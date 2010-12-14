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
import uk.org.sappho.code.change.management.data.persistence.ConfigurationRawDataPersistence;
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
import uk.org.sappho.string.mapping.Mapper;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.WarningList;

public class CodeChangeManagementApp extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(CodeChangeManagementApp.class);

    private WarningList warningList;
    private RawData rawData = new RawData();
    private Injector injector;
    private Mapper commitCommentToIssueKeyMapper;

    private final Configuration config;

    public CodeChangeManagementApp(Configuration config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        LOG.debug("Configuring plugins");

        try {
            warningList = new SimpleWarningList();
            bind(WarningList.class).toInstance(warningList);
            bind(Configuration.class).toInstance(config);

            // load data mapping scripts
            HeatMapSelector heatMapSelector = (HeatMapSelector) config.getGroovyScriptObject("mapper.heatmap.selector");
            bind(HeatMapSelector.class).toInstance(heatMapSelector);
            commitCommentToIssueKeyMapper = (Mapper) config.getGroovyScriptObject("mapper.commit.comment.to.issue.key");
            bind(SCM.class)
                    .to(config.<SCM> getPlugin("scm.plugin", "uk.org.sappho.code.change.management.scm"));
            bind(Report.class)
                    .to(config.<Report> getPlugin("report.plugin", "uk.org.sappho.code.heatmap.report"));
            bind(IssueManagement.class)
                    .to(config.<IssueManagement> getPlugin("issues.plugin",
                            "uk.org.sappho.code.change.management.issues"));
            bind(Engine.class).to(
                    config.<Engine> getPlugin("engine.plugin", "uk.org.sappho.code.heatmap.engine"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void refresh() throws ConfigurationException {

        LOG.info("Refreshing issue management data");
        IssueManagement issueManagement = injector.getInstance(IssueManagement.class);
        rawData.clearIssueData();
        for (String revisionKey : rawData.getRevisionKeys()) {
            RevisionData revisionData = rawData.getRevisionData(revisionKey);
            String commitComment = revisionData.getCommitComment().split("\n")[0];
            String issueKey = commitCommentToIssueKeyMapper.map(commitComment);
            if (issueKey != null) {
                IssueData issueData = issueManagement.getIssueData(issueKey);
                if (issueData != null) {
                    rawData.putIssueData(issueData);
                }
            } else {
                warningList.add("Issue not found", "Unable to find an issue to match revision " + revisionKey + " \""
                        + commitComment + "\"");
            }
        }
        rawData.reWire(commitCommentToIssueKeyMapper);
        for (String rawIssueType : issueManagement.getIssueTypeMappings().keySet()) {
            warningList.add("Issue type mapping", "Raw issue type \"" + rawIssueType + "\" mapped to \""
                    + issueManagement.getIssueTypeMappings().get(rawIssueType) + "\"");
        }
        for (String rawRelease : issueManagement.getReleaseMappings().keySet()) {
            warningList.add("Release mapping", "Raw release \"" + rawRelease + "\" mapped to \""
                    + issueManagement.getReleaseMappings().get(rawRelease) + "\"");
        }
        rawData.putWarnings(warningList);
    }

    protected void run() throws ConfigurationException, IOException, IssueManagementException, SCMException,
            EngineException {

        injector = Guice.createInjector(this);
        ConfigurationRawDataPersistence rawDataPersistence = injector
                .getInstance(ConfigurationRawDataPersistence.class);

        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            LOG.info("Running " + action);
            if (action.equalsIgnoreCase("load")) {
                rawData = rawDataPersistence.load(config);
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
            Configuration config = new SimpleConfiguration();
            for (String configFilename : args) {
                config.load(configFilename);
            }
            new CodeChangeManagementApp(config).run();
            LOG.info("Everything done");
        } catch (Throwable t) {
            LOG.error("Application error", t);
        }
    }
}
