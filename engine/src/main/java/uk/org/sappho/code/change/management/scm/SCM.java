package uk.org.sappho.code.change.management.scm;

import uk.org.sappho.code.change.management.data.RawData;

public interface SCM {

    public void scan(RawData rawData) throws SCMException;
}
