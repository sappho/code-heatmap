package uk.org.sappho.code.heatmap.mapping;

import java.util.List;

public interface HeatMapSelector {

    public List<HeatMapMapping> map(String changedFile);
}
