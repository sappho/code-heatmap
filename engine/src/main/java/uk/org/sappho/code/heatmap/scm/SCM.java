package uk.org.sappho.code.heatmap.scm;

import uk.org.sappho.code.heatmap.engine.Releases;

public interface SCM {

    public void processChanges(Releases heatMapsForReleases) throws SCMException;
}
