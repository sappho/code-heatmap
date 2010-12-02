package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.warnings.Warnings;
import uk.org.sappho.code.heatmap.warnings.impl.Warning;

public class WarningsList implements Warnings {

    private final Map<String, List<Warning>> warnings = new HashMap<String, List<Warning>>();
    private static final Logger LOG = Logger.getLogger(WarningsList.class);

    @Inject
    public WarningsList() {

        LOG.info("Using simple warning logging plugin");
    }

    public void add(Warning warning) {

        String type = warning.getTypeName();
        LOG.info(type + ": " + warning);
        List<Warning> list = warnings.get(type);
        if (list == null) {
            list = new Vector<Warning>();
            warnings.put(type, list);
        }
        list.add(warning);
    }

    public Set<String> getTypes() {

        return warnings.keySet();
    }

    public List<Warning> getWarnings(String type) {

        return warnings.get(type);
    }
}
