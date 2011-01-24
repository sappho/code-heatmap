package uk.org.sappho.code.change.management.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.Warnings;
import uk.org.sappho.code.change.management.data.persistence.RawDataPersistence;
import uk.org.sappho.code.change.management.issues.IssueManagement;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.code.change.management.scm.SCM;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.string.mapping.Mapper;

public class StandardEngine implements Engine {

    private RawData rawData = new RawData();
    private final Configuration config;
    private final Provider<RawDataPersistence> rawDataPersistenceProvider;
    private final Provider<SCM> scmProvider;
    private final Provider<IssueManagement> issueManagementProvider;
    private final Provider<RawDataProcessing> rawDataProcessingProvider;
    private static final Logger log = Logger.getLogger(StandardEngine.class);

    @Inject
    public StandardEngine(Configuration config, Provider<RawDataPersistence> rawDataPersistenceProvider,
            Provider<SCM> scmProvider, Provider<RawDataProcessing> rawDataProcessingProvider,
            Provider<IssueManagement> issueManagementProvider) {

        this.config = config;
        this.rawDataPersistenceProvider = rawDataPersistenceProvider;
        this.scmProvider = scmProvider;
        this.issueManagementProvider = issueManagementProvider;
        this.rawDataProcessingProvider = rawDataProcessingProvider;
    }

    public void loadRawData() throws IOException, ConfigurationException {

        rawData = rawDataPersistenceProvider.get().load();
    }

    public void validateRawData() throws Exception {

        log.info("Validating data");
        if (rawData.isValid())
            log.info("Data is clean");
        else
            throw new Exception("Data is invalid");
    }

    public void saveRawData() throws IOException, ConfigurationException {

        rawDataPersistenceProvider.get().save(rawData);
    }

    public void scanSCM() throws SCMException, ConfigurationException, IssueManagementException {

        scmProvider.get().scan(rawData);
        refreshRawData();
    }

    public void processRawData() throws RawDataProcessingException, ConfigurationException {

        rawDataProcessingProvider.get().run(rawData);
    }

    public void refreshRawData() throws ConfigurationException, IssueManagementException {

        log.info("Refreshing issues connected with SCM revisions");

        // fresh warnings to be added to any already issued
        Warnings warnings = rawData.getWarnings();

        // get all the plugins and scripts needed
        IssueManagement issueManagement = issueManagementProvider.get();
        issueManagement.init(warnings);
        Mapper commitCommentToIssueKeyMapper = (Mapper) config
                .getGroovyScriptObject("mapper.commit.comment.to.issue.key");
        Mapper releaseMapper = (Mapper) config.getGroovyScriptObject("mapper.raw.release.to.release");
        Mapper typeMapper = (Mapper) config.getGroovyScriptObject("mapper.issue.type");
        Mapper priorityMapper = (Mapper) config.getGroovyScriptObject("mapper.issue.priority");
        Mapper resolutionMapper = (Mapper) config.getGroovyScriptObject("mapper.issue.resolution");
        Mapper statusMapper = (Mapper) config.getGroovyScriptObject("mapper.issue.status");
        Mapper personMapper = (Mapper) config.getGroovyScriptObject("mapper.person.id");

        // clear out all the issue data from any previous refresh that might have already been done
        rawData.clearIssueData();

        // run through all the stored revisions to pick up fresh linked issue data
        for (String revisionKey : rawData.getRevisionKeys()) {
            RevisionData revisionData = rawData.getRevisionData(revisionKey);
            String commitComment = revisionData.getCommitComment().split("\n")[0];
            String issueKey = commitCommentToIssueKeyMapper.map(commitComment);
            revisionData.setIssueKey(issueKey);
            if (issueKey != null) {
                if (rawData.getIssueData(issueKey) == null) {
                    IssueData issueData = issueManagement.getIssueData(issueKey);
                    if (issueData != null)
                        rawData.putIssueData(issueKey, issueData);
                    else
                        warnings.add("No issue data", "Unable to get issue data for revision " + revisionKey
                                + " issue " + issueKey);
                }
            } else {
                warnings.add("Issue key not found", "Unable to find an issue key for revision " + revisionKey + " \""
                        + commitComment + "\"");
            }
        }

        // set up a map of raw release names to meaningful cooked ones
        Map<String, String> releaseMappings = new HashMap<String, String>();
        for (String rawRelease : issueManagement.getRawReleases()) {
            String cookedRelease = releaseMapper.map(rawRelease);
            if (cookedRelease != null) {
                warnings.add("Release mapping", "Raw release \"" + rawRelease + "\" mapped to \"" + cookedRelease
                        + "\"");
                releaseMappings.put(rawRelease, cookedRelease);
            } else {
                warnings.add("Release ignored", "Raw release \"" + rawRelease + "\" will be ignored");
            }
        }
        // run through all the retrieved issues setting mapped types and releases
        for (String issueKey : rawData.getUnmappedIssueKeys()) {
            IssueData issueData = rawData.getUnmappedIssueData(issueKey);
            String rawAssignee = issueData.getAssignee();
            String mappedAssignee = personMapper.map(rawAssignee);
            warnings.add("Issue assignee mapping", "Raw assignee \"" + rawAssignee + "\" mapped to \"" + mappedAssignee
                    + "\"");
            String rawType = issueData.getType();
            String mappedType = typeMapper.map(rawType);
            warnings.add("Issue type mapping", "Raw type \"" + rawType + "\" mapped to \"" + mappedType + "\"");
            String rawPriority = issueData.getPriority();
            String mappedPriority = priorityMapper.map(rawPriority);
            warnings.add("Issue priority mapping", "Raw priority \"" + rawPriority + "\" mapped to \"" + mappedPriority
                    + "\"");
            String rawResolution = issueData.getResolution();
            String mappedResolution = resolutionMapper.map(rawResolution);
            warnings.add("Issue resolution mapping", "Raw resolution \"" + rawResolution + "\" mapped to \""
                    + mappedResolution + "\"");
            String rawStatus = issueData.getStatus();
            String mappedStatus = statusMapper.map(rawStatus);
            warnings.add("Issue status mapping", "Raw status \"" + rawStatus + "\" mapped to \"" + mappedStatus
                    + "\"");
            issueData.setNewData(mappedAssignee, mappedType, mappedPriority, mappedResolution, mappedStatus);
            List<String> rawReleases = issueData.getReleases();
            List<String> cookedReleases = new ArrayList<String>();
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
                warnings.add("Issue with no releases", "Issue " + issueKey + " has no association to a release");
            } else if (rawReleases.size() != 1) {
                warnings.add("Issue with multiple releases", "Issue " + issueKey
                        + " is associated with more than one raw release"
                        + rawReleasesStr + " mapping to" + cookedReleasesStr);
            }
        }
    }
}
