package uk.org.sappho.code.change.management.app;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.mapping.CommitCommentToIssueKeyMapper;
import uk.org.sappho.code.change.management.data.persistence.RawDataPersistence;
import uk.org.sappho.code.change.management.issues.IssueManagement;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.code.change.management.scm.SCM;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.code.heatmap.engine.Engine;
import uk.org.sappho.code.heatmap.engine.EngineException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.configuration.SimpleConfiguration;
import uk.org.sappho.warnings.SimpleWarningsList;
import uk.org.sappho.warnings.WarningsList;

public class CodeChangeManagementApp extends AbstractModule {

    private final String[] args;
    private SimpleConfiguration config;
    private RawData rawData = new RawData();
    private Injector injector;
    private static final Logger LOG = Logger.getLogger(CodeChangeManagementApp.class);

    public CodeChangeManagementApp(String[] args) {

        this.args = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Configuring plugins");
            bind(WarningsList.class).toInstance(new SimpleWarningsList());
            config = new SimpleConfiguration();
            for (String configFilename : args)
                config.load(configFilename);
            bind(Configuration.class).toInstance(config);
            bind(SCM.class).to(
                    (Class<? extends SCM>) config.getPlugin("scm.plugin", "uk.org.sappho.code.change.management.scm"));
            bind(CommitCommentToIssueKeyMapper.class).to(
                    (Class<? extends CommitCommentToIssueKeyMapper>) config.getPlugin(
                            "commit.comment.to.issue.key.mapper.plugin",
                            "uk.org.sappho.code.change.management.issues"));
            bind(Report.class).to(
                    (Class<? extends Report>) config.getPlugin("report.plugin", "uk.org.sappho.code.heatmap.report"));
            bind(IssueManagement.class).to(
                    (Class<? extends IssueManagement>) config.getPlugin("issues.plugin",
                            "uk.org.sappho.code.change.management.issues"));
            bind(Engine.class).to(
                    (Class<? extends Engine>) config.getPlugin("engine.plugin", "uk.org.sappho.code.heatmap.engine"));
            bind(HeatMapSelector.class).to(
                    (Class<? extends HeatMapSelector>) config.getPlugin("mapping.heatmap.selector.plugin",
                            "uk.org.sappho.code.heatmap.mapping"));
        } catch (Throwable t) {
            LOG.error("Unable to load plugins", t);
        }
    }

    protected void refresh() {

        rawData.clearIssueData();
        IssueManagement issueManagement = injector.getInstance(IssueManagement.class);
        CommitCommentToIssueKeyMapper commitCommentToIssueKeyMapper = injector
                .getInstance(CommitCommentToIssueKeyMapper.class);
        for (String revisionKey : rawData.getRevisionKeys()) {
            RevisionData revisionData = rawData.getRevisionData(revisionKey);
            String issueKey = commitCommentToIssueKeyMapper.getIssueKeyFromCommitComment(revisionKey,
                    revisionData.getCommitComment());
            IssueData issueData = issueManagement.getIssueData(issueKey);
            if (issueData != null) {
                rawData.putIssueData(issueData);
            }
        }
        rawData.reWire(commitCommentToIssueKeyMapper);
    }

    protected void run() throws ConfigurationException, IOException, IssueManagementException, SCMException,
            EngineException {

        injector = Guice.createInjector(this);
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
                scm.scan(rawData);
                refresh();
            } else if (action.equalsIgnoreCase("refresh")) {
                refresh();
            } else if (action.equalsIgnoreCase("process")) {
                Engine engine = injector.getInstance(Engine.class);
                engine.run(rawData);
            } else {
                throw new ConfigurationException("Action " + action + " is unrecognised");
            }
        }
    }

    public static void main(String[] args) {

        try {
            new CodeChangeManagementApp(args).run();
            LOG.info("Everything done");
        } catch (Throwable t) {
            LOG.error("Application error", t);
        }
    }
}
