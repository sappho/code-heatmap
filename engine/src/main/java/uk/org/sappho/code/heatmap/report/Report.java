package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.heatmap.engine.simple.SimpleHeatMapRawDataProcessing;
import uk.org.sappho.warnings.WarningList;

public interface Report {

    public void writeReport(SimpleHeatMapRawDataProcessing releases, WarningList warnings) throws ReportException;
}
