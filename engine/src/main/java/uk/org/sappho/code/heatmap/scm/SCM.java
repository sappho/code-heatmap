package uk.org.sappho.code.heatmap.scm;

import uk.org.sappho.code.heatmap.engine.HeatMaps;

public interface SCM {

    public void processChanges(HeatMaps heatMapCollection) throws SCMException;
}
