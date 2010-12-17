package uk.org.sappho.code.heatmap.engine.simple;

import uk.org.sappho.code.change.management.engine.EngineModule;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.configuration.ConfigurationException;

public class SimpleHeatMapEngineModule extends EngineModule {

    @Override
    protected void configure() {

        super.configure();
        try {
            bind(Report.class)
                    .to(getConfiguration().<Report> getPlugin("report.plugin", "uk.org.sappho.code.heatmap.report"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
