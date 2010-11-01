package uk.org.sappho.code.heatmap.app;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.engine.CodeHeatMapEngine;
import uk.org.sappho.code.heatmap.engine.Engine;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.scm.SCM;

public class CodeHeatMapApp extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(CodeHeatMapApp.class);

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Loading plugins");
            bind(SCM.class).to(
                    (Class<? extends SCM>) Class.forName("uk.org.sappho.code.heatmap.scm.Subversion"));
            bind(Report.class).to(
                    (Class<? extends Report>) Class.forName("uk.org.sappho.code.heatmap.report.CSVReport"));
            LOG.debug("All plugins loaded");
        } catch (Throwable t) {
            LOG.error("Unable to bind plugin classes", t);
        }
    }

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new CodeHeatMapApp());
        Engine engine = injector.getInstance(CodeHeatMapEngine.class);
        engine.writeReport();
    }
}
