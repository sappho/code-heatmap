package uk.org.sappho.code.heatmap.warnings;

import java.util.List;
import java.util.Set;

import uk.org.sappho.code.heatmap.warnings.impl.Warning;

public interface Warnings {

    public void add(Warning warning);

    public Set<String> getTypes();

    public List<Warning> getWarnings(String type);
}
