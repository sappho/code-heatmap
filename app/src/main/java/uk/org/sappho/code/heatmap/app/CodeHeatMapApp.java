package uk.org.sappho.code.heatmap.app;

import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.engine.CodeHeatMapEngine;
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
                    (Class<? extends SCM>) Class.forName(System.getProperty("scm.implementation")));
            bind(Report.class).to(
                    (Class<? extends Report>) Class.forName(System.getProperty("report.implementation")));
            LOG.debug("All plugins loaded");
        } catch (Throwable t) {
            LOG.error("Unable to bind plugin classes", t);
        }
    }

    public static void main(String[] args) {

        if (args.length == 1) {
            try {
                String configFilename = args[0];
                Properties props = System.getProperties();
                props.load(new FileReader(configFilename));
                Injector injector = Guice.createInjector(new CodeHeatMapApp());
                CodeHeatMapEngine engine = injector.getInstance(CodeHeatMapEngine.class);
                engine.writeReport();
            } catch (Throwable t) {
                LOG.error("Application error!", t);
            }
        } else {
            LOG.error("A configuration filename must be passed as a parameter");
        }
    }
}