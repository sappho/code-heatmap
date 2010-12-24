package uk.org.sappho.code.change.management.engine;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.scm.SCM;
import uk.org.sappho.code.change.management.scm.SCMException;

public class FakeSCMPlugin implements SCM {

    @Inject
    public FakeSCMPlugin() {
    }

    public void scan(RawData rawData) throws SCMException {
    }
}
