package uk.org.sappho.warnings.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.warnings.Warnings;

@Singleton
public class SimpleWarningsList implements Warnings {

    private final Map<String, List<Warning>> warnings = new HashMap<String, List<Warning>>();
    private static final Logger LOG = Logger.getLogger(SimpleWarningsList.class);

    @Inject
    public SimpleWarningsList() {

        LOG.info("Using simple warning logging plugin");
    }

    public void add(Warning warning) {

        String type = warning.getTypeName();
        List<Warning> list = warnings.get(type);
        if (list == null) {
            list = new Vector<Warning>();
            warnings.put(type, list);
        }
        String warningText = warning.toString();
        for (Warning existingWarning : list) {
            if (warningText.equals(existingWarning.toString())) {
                return;
            }
        }
        LOG.info(type + ": " + warningText);
        list.add(warning);
    }

    public Set<String> getTypes() {

        return warnings.keySet();
    }

    public List<Warning> getWarnings(String type) {

        return warnings.get(type);
    }
}
