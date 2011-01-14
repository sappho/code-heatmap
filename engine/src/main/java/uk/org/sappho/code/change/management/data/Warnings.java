package uk.org.sappho.code.change.management.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.validation.WarningsConstraint;

public class Warnings {

    @WarningsConstraint
    private Map<String, List<String>> warningLists = new HashMap<String, List<String>>();

    private static final Logger log = Logger.getLogger(Warnings.class);

    public void add(String category, String warning) {

        add(category, warning, true);
    }

    public void add(String category, String warning, boolean logIt) {

        check();
        List<String> warnings = warningLists.get(category);
        if (warnings == null) {
            warnings = new ArrayList<String>();
            warningLists.put(category, warnings);
        }
        if (!warnings.contains(warning)) {
            if (logIt) {
                log.info(category + ": " + warning);
            }
            warnings.add(warning);
        }
    }

    public void add(Warnings warnings) {

        for (String category : warnings.getCategories()) {
            for (String warning : warnings.getWarnings(category)) {
                add(category, warning, false);
            }
        }
    }

    public Set<String> getCategories() {

        check();
        return warningLists.keySet();
    }

    public List<String> getWarnings(String category) {

        check();
        return warningLists.get(category);
    }

    private void check() {

        if (warningLists == null)
            warningLists = new HashMap<String, List<String>>();
    }
}
