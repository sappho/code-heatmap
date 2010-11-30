package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.heatmap.engine.HeatMaps;

public interface Report {

    public void writeReport(HeatMaps heatMapCollection) throws ReportException;
}
