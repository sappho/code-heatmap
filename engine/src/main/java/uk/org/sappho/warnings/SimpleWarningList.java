package uk.org.sappho.warnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.Validator;

import org.apache.log4j.Logger;

import uk.org.sappho.warnings.validation.WarningListConstraint;

public class SimpleWarningList implements WarningList {

    @WarningListConstraint
    private Map<String, List<String>> warningLists = new HashMap<String, List<String>>();

    private static final Logger log = Logger.getLogger(SimpleWarningList.class);

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

    public boolean isValid() {

        return new Validator().validate(this).size() == 0;
    }
}
