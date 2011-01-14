package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.change.management.data.Warnings;
import uk.org.sappho.code.heatmap.engine.simple.SimpleHeatMapRawDataProcessing;

public interface Report {

    public void writeReport(SimpleHeatMapRawDataProcessing releases, Warnings warnings) throws ReportException;
}
