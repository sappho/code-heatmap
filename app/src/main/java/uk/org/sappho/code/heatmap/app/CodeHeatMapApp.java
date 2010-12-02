package uk.org.sappho.code.heatmap.app;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.impl.ConfigurationFile;
import uk.org.sappho.code.heatmap.engine.Releases;
import uk.org.sappho.code.heatmap.engine.WarningsList;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.scm.SCM;
import uk.org.sappho.code.heatmap.warnings.Warnings;

public class CodeHeatMapApp extends AbstractModule {

    private final String commonPropertiesFilename;
    private final String instancePropertiesFilename;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapApp.class);

    public CodeHeatMapApp(String commonPropertiesFilename, String instancePropertiesFilename) {

        this.commonPropertiesFilename = commonPropertiesFilename;
        this.instancePropertiesFilename = instancePropertiesFilename;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Configuring plugins");
            bind(Warnings.class).toInstance(new WarningsList());
            ConfigurationFile config = new ConfigurationFile();
            config.load(commonPropertiesFilename);
            config.load(instancePropertiesFilename);
            bind(Configuration.class).toInstance(config);
            bind(SCM.class).to((Class<? extends SCM>) config.getPlugin("scm.plugin", "uk.org.sappho.code.heatmap.scm"));
            bind(Report.class).to(
                    (Class<? extends Report>) config.getPlugin("report.plugin", "uk.org.sappho.code.heatmap.report"));
            bind(IssueManagement.class).to(
                    (Class<? extends IssueManagement>) config.getPlugin("issues.plugin",
                            "uk.org.sappho.code.heatmap.issues"));
        } catch (Throwable t) {
            LOG.error("Unable to load plugins", t);
        }
    }

    public static void main(String[] args) {

        if (args.length == 2) {
            try {
                Injector injector = Guice.createInjector(new CodeHeatMapApp(args[0], args[1]));
                Releases releases = injector.getInstance(Releases.class);
                SCM scm = injector.getInstance(SCM.class);
                scm.processChanges(releases);
                Report report = injector.getInstance(Report.class);
                report.writeReport(releases);
            } catch (Throwable t) {
                LOG.error("Application error", t);
            }
        } else {
            LOG.info("Specify the names of a common and instance configuration file on the command line");
        }
    }
}
