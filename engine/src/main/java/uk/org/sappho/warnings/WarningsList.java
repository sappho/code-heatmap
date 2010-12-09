package uk.org.sappho.warnings;

import java.util.List;
import java.util.Set;


public interface WarningsList {

    public void add(Warning warning);

    public Set<String> getTypes();

    public List<Warning> getWarnings(String type);
}
