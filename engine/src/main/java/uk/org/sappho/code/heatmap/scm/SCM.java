package uk.org.sappho.code.heatmap.scm;

import uk.org.sappho.code.heatmap.engine.HeatMapCollection;

public interface SCM {

    public HeatMapCollection processChanges() throws SCMException;
}
