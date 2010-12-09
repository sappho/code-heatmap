package uk.org.sappho.code.change.management.issues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.issues.IssueManagement;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.jira4j.soap.GetParentService;
import uk.org.sappho.jira4j.soap.JiraSoapService;
import uk.org.sappho.warnings.MessageWarning;
import uk.org.sappho.warnings.WarningsList;

@Singleton
public class Jira implements IssueManagement {

    protected String jiraURL = null;
    protected JiraSoapService jiraSoapService = null;
    protected GetParentService getParentService = null;
    protected Map<String, RemoteIssue> mappedRemoteIssues = new HashMap<String, RemoteIssue>();
    protected Map<String, IssueData> allowedIssues = new HashMap<String, IssueData>();
    protected Map<String, String> warnedSubTasks = new HashMap<String, String>();
    protected Map<String, String> releases = new HashMap<String, String>();
    protected Map<String, String> issueTypes = new HashMap<String, String>();
    protected WarningsList warnings;
    protected Configuration config;
    protected static final Pattern SIMPLE_JIRA_REGEX = Pattern.compile("^([a-zA-Z]{2,}-\\d+):.*$");
    private static final Logger LOG = Logger.getLogger(Jira.class);
    protected static final String ISSUE_FIELDS = "Issue fields";
    protected static final String NO_RELEASE = "missing";

    @Inject
    public Jira(WarningsList warnings, Configuration config) throws IssueManagementException {

        LOG.info("Using Jira issue management plugin");
        this.warnings = warnings;
        this.config = config;
    }

    public void init() throws IssueManagementException {

        if (jiraURL == null) {
            connect();
            if (getParentService == null) {
                preFetchAllowedIssues();
            }
        }
    }

    protected void connect() throws IssueManagementException {

        jiraURL = config.getProperty("jira.url", "http://example.com");
        String username = config.getProperty("jira.username", "nobody");
        String password = config.getProperty("jira.password", "nopassword");
        LOG.info("Connecting to " + jiraURL + " as " + username);
        try {
            jiraSoapService = new JiraSoapService(jiraURL, username, password);
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to log in to Jira at " + jiraURL + " as user " + username, t);
        }
        try {
            getParentService = new GetParentService(jiraURL, username, password);
            LOG.info("Using optional GetParent SOAP web service");
        } catch (Throwable t) {
            LOG.info("GetParent SOAP web service is not installed or authentication failed");
            getParentService = null;
        }
    }

    protected void preFetchAllowedIssues() throws IssueManagementException {

        LOG.info("Getting list of allowed issues");
        try {
            // get all tasks we're prepared to deal with
            String jql = config.getProperty("jira.jql.issues.allowed");
            int jqlMax = Integer.parseInt(config.getProperty("jira.jql.issues.allowed.max", "1000"));
            LOG.info("Running Jira query (max. " + jqlMax + " issues): " + jql);
            RemoteIssue[] remoteIssues = jiraSoapService.getService().getIssuesFromJqlSearch(
                    jiraSoapService.getToken(), jql, jqlMax);
            LOG.info("Processing " + remoteIssues.length + " issues returned by query");
            // map all subtasks back to their parents
            Map<String, String> subTaskParents = new HashMap<String, String>();
            for (RemoteIssue remoteIssue : remoteIssues) {
                String issueKey = remoteIssue.getKey();
                addRemoteIssue(issueKey, remoteIssue);
                RemoteIssue[] remoteSubTasks = jiraSoapService.getService().getIssuesFromJqlSearch(
                        jiraSoapService.getToken(), "parent = " + issueKey, 200);
                for (RemoteIssue remoteSubTask : remoteSubTasks) {
                    String subTaskKey = remoteSubTask.getKey();
                    warnings.add(new JiraSubTaskMappingWarning(jiraURL, subTaskKey, issueKey));
                    addRemoteIssue(subTaskKey, remoteSubTask);
                    subTaskParents.put(subTaskKey, issueKey);
                }
            }
            // create issue wrappers for all allowed root (non-subtask) issues
            for (String issueKey : mappedRemoteIssues.keySet()) {
                String parentKey = subTaskParents.get(issueKey);
                addAllowedIssue(issueKey, parentKey);
            }
            LOG.info("Processed " + mappedRemoteIssues.size()
                    + " issues - added subtasks might have inflated this figure");
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to get list of allowed issues", t);
        }
    }

    protected void addRemoteIssue(String issueKey, RemoteIssue remoteIssue) {

        if (mappedRemoteIssues.get(issueKey) == null) {
            mappedRemoteIssues.put(issueKey, remoteIssue);
        }
    }

    protected void addAllowedIssue(String issueKey, String parentKey) throws IssueManagementException {

        IssueData issueWrapper = parentKey != null ?
                createIssueWrapper(mappedRemoteIssues.get(parentKey), issueKey) :
                createIssueWrapper(mappedRemoteIssues.get(issueKey), null);
        allowedIssues.put(issueKey, issueWrapper);
    }

    protected IssueData createIssueWrapper(RemoteIssue issue, String subTaskKey) throws IssueManagementException {

        List<String> issueReleases = new Vector<String>();
        Map<String, String> issueReleaseMap = new HashMap<String, String>();
        RemoteVersion[] fixVersions = issue.getFixVersions();
        if (fixVersions.length == 0) {
            issueReleaseMap.put(NO_RELEASE, NO_RELEASE);
        } else {
            for (RemoteVersion remoteVersion : fixVersions) {
                String remoteVersionName = remoteVersion.getName();
                String release = releases.get(remoteVersionName);
                if (release == null) {
                    try {
                        release = config.getProperty("jira.version.map.release." + remoteVersionName);
                    } catch (ConfigurationException e) {
                        release = "unknown";
                    }
                    warnings.add(new JiraVersionMappingWarning(jiraURL, issue.getKey(), remoteVersionName, release));
                    releases.put(remoteVersionName, release);
                }
                issueReleaseMap.put(release, release);
            }
        }
        for (String release : issueReleaseMap.keySet()) {
            issueReleases.add(release);
        }
        if (issueReleases.size() > 1) {
            warnings.add(new JiraIssueWithMultipleReleasesWarning(jiraURL, issue.getKey(), issueReleases));
        }
        String typeId = issue.getType();
        String typeName = issueTypes.get(typeId);
        if (typeName == null) {
            typeName = config.getProperty("jira.type.map.id." + typeId, "housekeeping");
            warnings.add(new JiraIssueTypeMappingWarning(jiraURL, issue.getKey(), typeId, typeName));
            issueTypes.put(typeId, typeName);
        }
        return new IssueData(issue.getKey(), issue.getSummary(), subTaskKey, issueReleases);
    }

    protected String getIssueKeyFromCommitComment(String commitComment) {

        String key = null;
        Matcher matcher = SIMPLE_JIRA_REGEX.matcher(commitComment.split("\n")[0]);
        if (matcher.matches()) {
            key = matcher.group(1);
        } else {
            warnings.add(new MessageWarning("No Jira issue key found in commit comment: " + commitComment));
        }
        return key;
    }

    public IssueData getIssue(String commitComment) {

        IssueData issue = null;
        String issueKey = getIssueKeyFromCommitComment(commitComment);
        if (issueKey != null) {
            issue = allowedIssues.get(issueKey);
            if (issue == null) {
                if (getParentService != null) {
                    try {
                        RemoteIssue remoteIssue = jiraSoapService.getService()
                                .getIssue(jiraSoapService.getToken(), issueKey);
                        addRemoteIssue(issueKey, remoteIssue);
                        String parentKey = getParentService.getParent(issueKey);
                        if (parentKey != null) {
                            RemoteIssue parentRemoteIssue = jiraSoapService.getService()
                                    .getIssue(jiraSoapService.getToken(), parentKey);
                            addRemoteIssue(parentKey, parentRemoteIssue);
                            if (warnedSubTasks.get(issueKey) == null) {
                                warnings.add(new JiraSubTaskMappingWarning(jiraURL, issueKey, parentKey));
                                warnedSubTasks.put(issueKey, issueKey);
                            }
                        }
                        addAllowedIssue(issueKey, parentKey);
                    } catch (Throwable t) {
                        warnings.add(new JiraIssueNotFoundWarning(jiraURL, issueKey));
                    }
                } else {
                    warnings.add(new JiraIssueNotFoundWarning(jiraURL, issueKey));
                }
            }
        }
        return issue;
    }
}
