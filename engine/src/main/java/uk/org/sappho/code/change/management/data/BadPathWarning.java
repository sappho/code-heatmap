package uk.org.sappho.code.change.management.data;

import uk.org.sappho.warnings.Warning;

public class BadPathWarning extends Warning {

    private final String path;

    public BadPathWarning(String path) {

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
