package uk.org.sappho.code.heatmap.basic;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.heatmap.HeatMap;
import uk.org.sappho.code.heatmap.HeatMapCollection;
import uk.org.sappho.code.heatmap.HeatMapItem;
import uk.org.sappho.code.heatmap.selection.HeatMapSelector;
import uk.org.sappho.code.heatmap.weight.WeightCalculator;

public class BasicHeatMapModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HeatMapCollection.class).to(BasicHeatMapCollection.class);
        bind(HeatMap.class).to(BasicHeatMap.class);
        bind(HeatMapItem.class).to(BasicHeatMapItem.class);
        bind(HeatMapSelector.class).to(BasicHeatMapSelector.class);
        bind(WeightCalculator.class).to(BasicWeightCalculator.class);
    }
}
