package uk.org.sappho.warnings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

public class SimpleWarningsList implements WarningsList {

    private final Map<String, List<String>> warningLists = new HashMap<String, List<String>>();
    private static final Logger log = Logger.getLogger(SimpleWarningsList.class);

    public void add(String category, String warning) {

        List<String> list = warningLists.get(category);
        if (list == null) {
            list = new Vector<String>();
            warningLists.put(category, list);
        }
        if (!list.contains(warning)) {
            log.info(category + ": " + warning);
            list.add(warning);
        }
    }

    public Set<String> getCategories() {

        return warningLists.keySet();
    }

    public List<String> getWarnings(String category) {

        return warningLists.get(category);
    }
}
