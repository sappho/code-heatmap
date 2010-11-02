package uk.org.sappho.code.heatmap.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.config.Config;
import uk.org.sappho.code.heatmap.engine.HeatMap;
import uk.org.sappho.code.heatmap.engine.HeatMapCollection;
import uk.org.sappho.code.heatmap.engine.HeatMapItem;

public class CSVReport implements Report {

    private static final Logger LOG = Logger.getLogger(CSVReport.class);

    @Inject
    public CSVReport() {

        LOG.debug("Using CSV Report plugin");
    }

    public void writeReport(HeatMapCollection heatMapCollection) throws ReportException {

        Writer writer = null;
        try {
            String basePath = Config.getConfig().getProperty("report.path");
            String extension = Config.getConfig().getProperty("report.extension", ".csv");
            String seperator = Config.getConfig().getProperty("csv.seperator", ",");
            String header = Config.getConfig().getProperty("csv.header",
                    "Item Name" + seperator + "Change Issue Count" + seperator
                            + "Change Count" + seperator + "Issues");
            LOG.debug("CSV Report parameters:");
            LOG.debug("basePath:  " + basePath);
            LOG.debug("extension: " + extension);
            LOG.debug("seperator: " + seperator);
            LOG.debug("header:    " + header);
            for (String heatMapName : HeatMapCollection.HEATMAPS) {
                String filename = basePath + heatMapName + extension;
                LOG.info("Writing CSV report " + filename);
                writer = new FileWriter(filename);
                writer.write(header + "\n");
                HeatMap heatMap = heatMapCollection.getHeatMap(heatMapName);
                for (HeatMapItem item : heatMap.getSortedList()) {
                    writer.write(item.getName() + seperator + item.jiraCount() + seperator + item.changeCount());
                    for (String issueId : item.getJiras()) {
                        writer.write(seperator + issueId);
                    }
                    writer.write("\n");
                }
                writer.close();
                LOG.debug("Written " + filename);
            }
        } catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            throw new ReportException("Unable to write CSV reports", t);
        }
    }
}
