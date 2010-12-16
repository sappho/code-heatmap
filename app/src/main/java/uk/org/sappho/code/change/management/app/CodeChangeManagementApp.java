package uk.org.sappho.code.change.management.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
        Mapper commitCommentToIssueKeyMapper = (Mapper) config
                .getGroovyScriptObject("mapper.commit.comment.to.issue.key");
        IssueManagement issueManagement = injector.getInstance(IssueManagement.class);
        // clear out issue data from a previous scan or refresh
        rawData.clearIssueData();
        // run through all the stored revisions to pick up fresh linked issue data
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
        // re-wire the newly acquired issue data into the revisions
        rawData.reWire(commitCommentToIssueKeyMapper);
        // set up a map of raw release names to meaningful cooked ones
        Mapper releaseMapper = (Mapper) config.getGroovyScriptObject("mapper.raw.release.to.release");
        Map<String, String> releaseMappings = new HashMap<String, String>();
        for (String rawRelease : issueManagement.getRawReleases()) {
            String cookedRelease = releaseMapper.map(rawRelease);
            if (cookedRelease != null) {
                warningList.add("Release mapping", "Raw release \"" + rawRelease + "\" mapped to \"" + cookedRelease
                        + "\"");
                releaseMappings.put(rawRelease, cookedRelease);
            } else {
                warningList.add("Release ignored", "Raw release \"" + rawRelease + "\" will be ignored");
            }
        }
        // run through all the retrieved issues settings mapped types and releases
        Mapper issueTypeMapper = (Mapper) config.getGroovyScriptObject("mapper.issue.type");
        for (String issueKey : rawData.getIssueKeys()) {
            IssueData issueData = rawData.getIssueData(issueKey);
            String rawType = issueData.getType();
            String cookedType = issueTypeMapper.map(rawType);
            warningList
                    .add("Issue type mapping", "Raw issue type \"" + rawType + "\" mapped to \"" + cookedType + "\"");
            issueData.setType(cookedType);
            List<String> rawReleases = issueData.getReleases();
            List<String> cookedReleases = new Vector<String>();
            String rawReleasesStr = "";
            String cookedReleasesStr = "";
            for (String rawRelease : rawReleases) {
                rawReleasesStr += " \"" + rawRelease + "\"";
                String cookedRelease = releaseMappings.get(rawRelease);
                if (cookedRelease != null) {
                    if (!cookedReleases.contains(cookedRelease)) {
                        cookedReleasesStr += " \"" + cookedRelease + "\"";
                        cookedReleases.add(cookedRelease);
                    }
                }
            }
            issueData.setReleases(cookedReleases);
            if (rawReleases.size() == 0) {
                warningList.add("Issue with no releases", "Issue " + issueKey + " has no association to a release");
            } else if (rawReleases.size() != 1) {
                warningList.add("Issue with multiple releases", "Issue " + issueKey
                        + " is associated with more than one raw release"
                        + rawReleasesStr + " mapping to" + cookedReleasesStr);
            }
        }
        rawData.putWarnings(warningList);
    }

    protected void run() throws ConfigurationException, IOException, IssueManagementException, SCMException,
            EngineException {

        injector = Guice.createInjector(this);
        ConfigurationRawDataPersistence rawDataPersistence = new ConfigurationRawDataPersistence(config);
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
