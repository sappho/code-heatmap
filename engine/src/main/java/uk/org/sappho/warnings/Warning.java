package uk.org.sappho.warnings;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class Warning {

    @NotNull
    @NotEmpty
    private final String warning;

    public Warning(String warning) {

        this.warning = warning;
    }

    public String getWarning() {
        return warning;
    }
}
