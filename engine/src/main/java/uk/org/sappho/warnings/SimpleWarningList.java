package uk.org.sappho.warnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.ValidationException;

public class SimpleWarningList implements WarningList {

    private final Map<String, List<String>> warningLists = new HashMap<String, List<String>>();
    private static final Logger log = Logger.getLogger(SimpleWarningList.class);

    public void add(String category, String warning) {

        add(category, warning, true);
    }

    public void add(String category, String warning, boolean logIt) {

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

    public Set<String> getCategories() {

        return warningLists.keySet();
    }

    public List<String> getWarnings(String category) {

        return warningLists.get(category);
    }

    public int compareTo(WarningList o) {

        int comparison = 1;
        return comparison;
    }

    public void checkValidity() throws ValidationException {

        if (warningLists == null) {
            throw new ValidationException("Missing warningLists");
        }
        for (String category : warningLists.keySet()) {
            List<String> warningList = warningLists.get(category);
            if (warningList == null) {
                throw new ValidationException("Missing warningList in category " + category);
            }
            for (String warning : warningList) {
                if (warning == null || warning.length() == 0) {
                    throw new ValidationException("Missing warning in category " + category);
                }
            }
        }
    }
}
