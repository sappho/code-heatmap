package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.heatmap.engine.HeatMapCollection;

public interface Report {

    public void writeReport(HeatMapCollection heatMapCollection);
}
