package uk.org.sappho.code.heatmap.basic;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.heatmap.selection.HeatMapMapping;
import uk.org.sappho.code.heatmap.selection.HeatMapSelector;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@Singleton
public class BasicHeatMapSelector implements HeatMapSelector {

    private final HeatMapSelector selector;

    @Inject
    public BasicHeatMapSelector(Configuration config) throws ConfigurationException {
        selector = (HeatMapSelector) config.getGroovyScriptObject("heatmap.selector");
    }

    public List<HeatMapMapping> map(String changedFile) {
        return selector.map(changedFile);
    }
}
