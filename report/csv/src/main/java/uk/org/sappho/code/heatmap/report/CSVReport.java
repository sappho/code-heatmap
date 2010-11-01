package uk.org.sappho.code.heatmap.report;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.engine.HeatMapCollection;
import uk.org.sappho.code.heatmap.report.Report;

public class CSVReport implements Report {

    private static final Logger LOG = Logger.getLogger(CSVReport.class);

    @Inject
    public CSVReport() {
    }

    public void writeReport(HeatMapCollection heatMapCollection) {

        LOG.debug("Writing report");
    }
}
