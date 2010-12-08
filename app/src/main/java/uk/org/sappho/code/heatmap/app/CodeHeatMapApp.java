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
import uk.org.sappho.code.heatmap.data.RawData;
import uk.org.sappho.code.heatmap.data.RawDataPersistence;
import uk.org.sappho.code.heatmap.engine.ChangeSet;
import uk.org.sappho.code.heatmap.engine.Engine;
import uk.org.sappho.code.heatmap.engine.EngineException;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.code.heatmap.report.Report;
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
            bind(Engine.class).to(
                    (Class<? extends Engine>) config.getPlugin("engine.plugin",
                            "uk.org.sappho.code.heatmap.engine"));
            bind(HeatMapSelector.class).to(
                    (Class<? extends HeatMapSelector>) config.getPlugin("mapping.heatmap.selector.plugin",
                            "uk.org.sappho.code.heatmap.mapping"));
        } catch (Throwable t) {
            LOG.error("Unable to load plugins", t);
        }
    }

    protected void run() throws ConfigurationException, IOException, IssueManagementException, SCMException,
            EngineException {

        Injector injector = Guice.createInjector(this);
        RawData rawData = new RawData();
        RawDataPersistence rawDataPersistence = new RawDataPersistence(config);
        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            LOG.info("Running " + action);
            if (action.equalsIgnoreCase("load")) {
                rawData = rawDataPersistence.load();
            } else if (action.equalsIgnoreCase("save")) {
                rawDataPersistence.save(rawData);
            } else if (action.equalsIgnoreCase("scan")) {
                SCM scm = injector.getInstance(SCM.class);
                List<ChangeSet> changeSets = scm.scan();
                rawData.add(changeSets);
            } else if (action.equalsIgnoreCase("report")) {
                Engine engine = injector.getInstance(Engine.class);
                engine.add(rawData.getChangeSets());
                engine.run();
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
