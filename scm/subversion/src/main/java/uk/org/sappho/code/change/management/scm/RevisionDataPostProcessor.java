package uk.org.sappho.code.change.management.scm;

import uk.org.sappho.code.change.management.data.RevisionData;

public interface RevisionDataPostProcessor {

    public void process(RevisionData revisionData);
}
