package uk.org.sappho.code.heatmap.mapping;

import java.util.List;

import uk.org.sappho.code.heatmap.engine.HeatMapMapping;

public interface HeatMapSelector {

    public List<HeatMapMapping> map(String changedFile);
}
