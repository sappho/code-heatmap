package uk.org.sappho.code.change.management.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.constraint.AssertValid;

import org.apache.log4j.Logger;

import uk.org.sappho.validation.MapKeysPopulatedConstraint;

public class WarningList {

    @MapKeysPopulatedConstraint
    @AssertValid
    private Map<String, List<String>> warningLists = new HashMap<String, List<String>>();

    private static final Logger log = Logger.getLogger(WarningList.class);

    public void add(String category, String warning) {

        add(category, warning, true);
    }

    public void add(String category, String warning, boolean logIt) {

        check();
        List<String> list = warningLists.get(category);
        if (list == null) {
            list = new ArrayList<String>();
            warningLists.put(category, list);
        }
        if (!list.contains(warning)) {
            if (logIt) {
                log.info(category + ": " + warning);
            }
            list.add(warning);
        }
    }

    public void add(WarningList warningList) {

        for (String category : warningList.getCategories()) {
            for (String warning : warningList.getWarnings(category)) {
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
