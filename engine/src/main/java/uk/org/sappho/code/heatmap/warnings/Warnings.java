package uk.org.sappho.code.heatmap.warnings;

import java.util.List;
import java.util.Set;

public interface Warnings {

    public void add(String type, String warning);

    public Set<String> getTypes();

    public List<String> getWarnings(String type);
}
