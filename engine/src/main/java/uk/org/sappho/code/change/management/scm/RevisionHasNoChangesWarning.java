package uk.org.sappho.code.change.management.scm;

import uk.org.sappho.warnings.Warning;

public class RevisionHasNoChangesWarning extends Warning {

    private final String revisionKey;

    public RevisionHasNoChangesWarning(String revisionKey) {

        this.revisionKey = revisionKey;
    }

    @Override
    public String getCategory() {

        return "Revision with no changes";
    }

    @Override
    public String toString() {

        return "No changes of interest for revision " + revisionKey;
    }
}
