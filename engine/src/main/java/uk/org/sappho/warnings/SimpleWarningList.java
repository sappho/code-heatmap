package uk.org.sappho.warnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.Validator;
import net.sf.oval.constraint.AssertValid;

import org.apache.log4j.Logger;

import uk.org.sappho.warnings.validation.WarningListConstraint;

public class SimpleWarningList implements WarningList {

    @WarningListConstraint
    @AssertValid
    private Map<String, List<Warning>> warningLists = new HashMap<String, List<Warning>>();

    private static final Logger log = Logger.getLogger(SimpleWarningList.class);

    public void add(String category, String warning) {

        add(category, warning, true);
    }

    public void add(String category, String warning, boolean logIt) {

        check();
        List<Warning> list = warningLists.get(category);
        if (list == null) {
            list = new ArrayList<Warning>();
            warningLists.put(category, list);
        }
        if (!list.contains(warning)) {
            if (logIt) {
                log.info(category + ": " + warning);
            }
            list.add(new Warning(warning));
        }
    }

    public Set<String> getCategories() {

        check();
        return warningLists.keySet();
    }

    public List<Warning> getWarnings(String category) {

        check();
        return warningLists.get(category);
    }

    private void check() {

        if (warningLists == null)
            warningLists = new HashMap<String, List<Warning>>();
    }

    public boolean isValid() {

        return new Validator().validate(this).size() == 0;
    }
}
