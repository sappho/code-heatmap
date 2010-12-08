package uk.org.sappho.code.heatmap.scm;

import java.util.List;

import uk.org.sappho.code.heatmap.engine.ChangeSet;

public interface SCM {

    public List<ChangeSet> scan() throws SCMException;
}
