package uk.org.sappho.warnings;

import java.util.List;
import java.util.Set;

import uk.org.sappho.warnings.simple.Warning;

public interface Warnings {

    public void add(Warning warning);

    public Set<String> getTypes();

    public List<Warning> getWarnings(String type);
}
