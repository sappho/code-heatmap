package uk.org.sappho.code.change.management.scm;

import uk.org.sappho.warnings.Warning;

public class SubversionPathWarning extends Warning {

    private final String path;

    public SubversionPathWarning(String path) {

        this.path = path;
    }

    @Override
    public String getCategory() {

        return "Subversion repository path";
    }

    @Override
    public String toString() {

        return "Unable to work out if " + path + " is a file or directory";
    }
}
