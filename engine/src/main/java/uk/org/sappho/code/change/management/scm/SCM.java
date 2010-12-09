package uk.org.sappho.code.change.management.scm;

import java.util.List;

import uk.org.sappho.code.change.management.data.ChangeSet;

public interface SCM {

    public List<ChangeSet> scan() throws SCMException;
}
