package uk.org.sappho.code.heatmap.app;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;
import uk.org.sappho.code.heatmap.config.impl.SimpleConfiguration;
import uk.org.sappho.code.heatmap.engine.Releases;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.report.ReportException;
import uk.org.sappho.code.heatmap.scm.SCM;
import uk.org.sappho.code.heatmap.scm.SCMException;
import uk.org.sappho.code.heatmap.warnings.Warnings;
import uk.org.sappho.code.heatmap.warnings.impl.SimpleWarningsList;

public class CodeHeatMapApp extends AbstractModule {

    private final String[] args;
    private SimpleConfiguration config;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapApp.class);

    public CodeHeatMapApp(String[] args) {

        this.args = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Configuring plugins");
            bind(Warnings.class).toInstance(new SimpleWarningsList());
            config = new SimpleConfiguration();
            for (String configFilename : args)
                config.load(configFilename);
            bind(Configuration.class).toInstance(config);
            bind(SCM.class).to((Class<? extends SCM>) config.getPlugin("scm.plugin", "uk.org.sappho.code.heatmap.scm"));
            bind(Report.class).to(
                    (Class<? extends Report>) config.getPlugin("report.plugin", "uk.org.sappho.code.heatmap.report"));
            bind(IssueManagement.class).to(
                    (Class<? extends IssueManagement>) config.getPlugin("issues.plugin",
                            "uk.org.sappho.code.heatmap.issues"));
            bind(HeatMapSelector.class).to(
                    (Class<? extends HeatMapSelector>) config.getPlugin("mapping.heatmap.selector.plugin",
                            "uk.org.sappho.code.heatmap.mapping"));
        } catch (Throwable t) {
            LOG.error("Unable to load plugins", t);
        }
    }

    protected void run() throws SCMException, ReportException, ConfigurationException, IOException,
            IssueManagementException {

        Injector injector = Guice.createInjector(this);
        Releases releases = injector.getInstance(Releases.class);
        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            LOG.info("Running " + action);
            if (action.equalsIgnoreCase("load")) {
                releases.load();
            } else if (action.equalsIgnoreCase("save")) {
                releases.save();
            } else if (action.equalsIgnoreCase("scan")) {
                SCM scm = injector.getInstance(SCM.class);
                scm.processChanges(releases);
            } else if (action.equalsIgnoreCase("report")) {
                Report report = injector.getInstance(Report.class);
                report.writeReport(releases);
            } else {
                throw new ConfigurationException("Action " + action + " is unrecognised");
            }
        }
    }

    public static void main(String[] args) {

        try {
            new CodeHeatMapApp(args).run();
            LOG.info("Everything done");
        } catch (Throwable t) {
            LOG.error("Application error", t);
        }
    }
}
