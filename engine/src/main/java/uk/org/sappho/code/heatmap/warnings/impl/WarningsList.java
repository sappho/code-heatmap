package uk.org.sappho.code.heatmap.warnings.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.heatmap.warnings.Warnings;

@Singleton
public class WarningsList implements Warnings {

    private final Map<String, List<String>> warnings = new HashMap<String, List<String>>();
    private static final Logger LOG = Logger.getLogger(WarningsList.class);

    @Inject
    public WarningsList() {

        LOG.info("Using simple warning logging plugin");
    }

    public void add(String type, String warning) {

        LOG.info(type + ": " + warning);
        List<String> list = warnings.get(type);
        if (list == null) {
            list = new Vector<String>();
            warnings.put(type, list);
        }
        list.add(warning);
    }

    public Set<String> getTypes() {

        return warnings.keySet();
    }

    public List<String> getWarnings(String type) {

        return warnings.get(type);
    }
}
