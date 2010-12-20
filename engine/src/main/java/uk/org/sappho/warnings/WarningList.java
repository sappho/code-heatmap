package uk.org.sappho.warnings;

import java.util.List;
import java.util.Set;

import uk.org.sappho.code.change.management.data.Validatable;

public interface WarningList extends Comparable<WarningList>, Validatable {

    public void add(String category, String warning);

    public void add(String category, String warning, boolean logIt);

    public Set<String> getCategories();

    public List<String> getWarnings(String category);
}
