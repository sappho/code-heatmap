package uk.org.sappho.code.heatmap.basic;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.heatmap.HeatMap;
import uk.org.sappho.code.heatmap.HeatMapCollection;
import uk.org.sappho.code.heatmap.HeatMapItem;
import uk.org.sappho.code.heatmap.selection.HeatMapSelector;
import uk.org.sappho.code.heatmap.weight.WeightCalculator;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

public class BasicHeatMapModule extends AbstractModule {

    private final Configuration config;

    public BasicHeatMapModule(Configuration config) {

        this.config = config;
    }

    @Override
    protected void configure() {

        try {
            bind(HeatMapCollection.class).to(BasicHeatMapCollection.class);
            bind(HeatMap.class).to(BasicHeatMap.class);
            bind(HeatMapItem.class).to(BasicHeatMapItem.class);
            bind(HeatMapSelector.class).toInstance(
                    (HeatMapSelector) config.getGroovyScriptObject("heatmap.selector"));
            bind(WeightCalculator.class).to(BasicWeightCalculator.class);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
