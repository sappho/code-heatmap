package uk.org.sappho.code.heatmap.scm;

import uk.org.sappho.code.heatmap.warnings.impl.Warning;

public class SubversionPathWarning extends Warning {

    private final String path;

    public SubversionPathWarning(String path) {

        this.path = path;
    }

    @Override
    public String getTypeName() {

        return "Subversion repository path";
    }

    @Override
    public String toString() {

        return "Unable to work out if " + path + " is a file or directory";
    }
}