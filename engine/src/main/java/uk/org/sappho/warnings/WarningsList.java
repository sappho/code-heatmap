package uk.org.sappho.warnings;

import java.util.List;
import java.util.Set;

public interface WarningsList {

    public void add(String category, String warning);

    public Set<String> getCategories();

    public List<String> getWarnings(String category);
}
