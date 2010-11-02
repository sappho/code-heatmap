package uk.org.sappho.code.heatmap.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.engine.HeatMap;
import uk.org.sappho.code.heatmap.engine.HeatMapCollection;
import uk.org.sappho.code.heatmap.engine.HeatMapItem;

public class CSVReport implements Report {

    private final String basePath;
    private final String extension;
    private final String seperator;
    private final String header;
    private static final Logger LOG = Logger.getLogger(CSVReport.class);

    @Inject
    public CSVReport() {

        LOG.debug("Using CSV Report plugin");
        basePath = System.getProperty("report.path");
        extension = System.getProperty("report.extension", ".csv");
        seperator = System.getProperty("csv.seperator", ",");
        header = System.getProperty("csv.header", "Item Name" + seperator + "Change Issue Count" + seperator
                + "Change Count" + seperator + "Issues");
        LOG.debug("basePath:  " + basePath);
        LOG.debug("extension: " + extension);
        LOG.debug("seperator: " + seperator);
        LOG.debug("header:    " + header);
    }

    public void writeReport(HeatMapCollection heatMapCollection) {

        Writer writer = null;
        try {
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
            LOG.error("Unable to write report!", t);
        }
    }
}
