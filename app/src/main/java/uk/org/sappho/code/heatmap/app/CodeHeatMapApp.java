package uk.org.sappho.code.heatmap.app;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.impl.ConfigurationFile;
import uk.org.sappho.code.heatmap.engine.CodeHeatMapEngine;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.scm.SCM;

public class CodeHeatMapApp extends AbstractModule {

    private final String filename;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapApp.class);

    public CodeHeatMapApp(String filename) {

        this.filename = filename;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Configuring plugins");
            ConfigurationFile config = new ConfigurationFile(filename);
            bind(Configuration.class).toInstance(config);
            bind(SCM.class).to((Class<? extends SCM>) Class.forName(config.getProperty("scm.plugin")));
            bind(Report.class).to((Class<? extends Report>) Class.forName(config.getProperty("report.plugin")));
            bind(IssueManagement.class).to(
                    (Class<? extends IssueManagement>) Class.forName(config.getProperty("issues.plugin")));
        } catch (Throwable t) {
            // ignore this because it gives little away and errors are properly caught in main
        }
    }

    public static void main(String[] args) {

        if (args.length == 1) {
            try {
                Injector injector = Guice.createInjector(new CodeHeatMapApp(args[0]));
                CodeHeatMapEngine engine = injector.getInstance(CodeHeatMapEngine.class);
                engine.writeReport();
            } catch (Throwable t) {
                LOG.error("Application error", t);
            }
        } else {
            LOG.info("Specify the name of a configuration file on the command line");
        }
    }
}
