package uk.org.sappho.warnings;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class Warning {

    @NotNull
    @NotEmpty
    private String warning;

    public Warning() {
    }

    public Warning(String warning) {

        this.warning = warning;
    }

    public String getWarning() {
        return warning;
    }

    @Override
    public String toString() {

        return warning != null ? warning.split("\n")[0] : "null";
    }
}
