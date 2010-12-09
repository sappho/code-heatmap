package uk.org.sappho.code.change.management.issues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.jira4j.soap.GetParentService;
import uk.org.sappho.jira4j.soap.JiraSoapService;
import uk.org.sappho.warnings.WarningsList;

@Singleton
public class Jira implements IssueManagement {

    protected String jiraURL = null;
    protected JiraSoapService jiraSoapService = null;
    protected GetParentService getParentService = null;
    protected Map<String, RemoteIssue> mappedRemoteIssues = new HashMap<String, RemoteIssue>();
    protected Map<String, IssueData> parentIssues = new HashMap<String, IssueData>();
    protected Map<String, String> subTaskParents = new HashMap<String, String>();
    protected Map<String, String> warnedSubTasks = new HashMap<String, String>();
    protected Map<String, String> releases = new HashMap<String, String>();
    protected Map<String, String> issueTypes = new HashMap<String, String>();
    protected WarningsList warnings;
    protected Configuration config;
    private static final Logger LOG = Logger.getLogger(Jira.class);
    protected static final String ISSUE_FIELDS = "Issue fields";
    protected static final String NO_RELEASE = "missing";

    @Inject
    public Jira(WarningsList warnings, Configuration config) throws IssueManagementException {

        LOG.info("Using Jira issue management plugin");
        this.warnings = warnings;
        this.config = config;
        connect();
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
            preFetchIssues();
        }
    }

    protected void preFetchIssues() throws IssueManagementException {

        try {
            String jql = config.getProperty("jira.jql.issues.allowed");
            int jqlMax = Integer.parseInt(config.getProperty("jira.jql.issues.allowed.max", "1000"));
            LOG.info("Running JQL query (max. " + jqlMax + " issues): " + jql);
            RemoteIssue[] remoteIssues = jiraSoapService.getService().getIssuesFromJqlSearch(
                    jiraSoapService.getToken(), jql, jqlMax);
            LOG.info("Processing " + remoteIssues.length + " issues returned by JQL query");
            for (RemoteIssue remoteIssue : remoteIssues) {
                String issueKey = remoteIssue.getKey();
                mappedRemoteIssues.put(issueKey, remoteIssue);
                RemoteIssue[] remoteSubTasks = jiraSoapService.getService().getIssuesFromJqlSearch(
                        jiraSoapService.getToken(), "parent = " + issueKey, 500);
                for (RemoteIssue remoteSubTask : remoteSubTasks) {
                    String subTaskKey = remoteSubTask.getKey();
                    if (mappedRemoteIssues.get(subTaskKey) == null) {
                        mappedRemoteIssues.put(subTaskKey, remoteSubTask);
                    }
                    subTaskParents.put(subTaskKey, issueKey);
                }
            }
            LOG.info("Processed " + mappedRemoteIssues.size()
                    + " issues - added subtasks might have inflated this figure");
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to pre-fetch issues", t);
        }
    }

    public IssueData getIssueData(String issueKey) {

        String subTaskKey = null;
        if (parentIssues.get(issueKey) == null) {
            String parentKey = null;
            if (getParentService != null) {
                try {
                    parentKey = getParentService.getParent(issueKey);
                } catch (Exception e) {
                    warnings.add(new JiraParentIssueFetchWarning(jiraURL, issueKey));
                }
            } else {
                parentKey = subTaskParents.get(issueKey);
            }
            if (parentKey != null) {
                subTaskKey = issueKey;
                issueKey = parentKey;
            }
        }
        IssueData issueData = parentIssues.get(issueKey);
        if (issueData == null) {
            RemoteIssue remoteIssue = mappedRemoteIssues.get(issueKey);
            if (remoteIssue == null) {
                try {
                    remoteIssue = jiraSoapService.getService().getIssue(jiraSoapService.getToken(), issueKey);
                } catch (Exception e) {
                    warnings.add(new JiraIssueNotFoundWarning(jiraURL, issueKey));
                }
                mappedRemoteIssues.put(issueKey, remoteIssue);
            }
            if (remoteIssue != null) {
                List<String> issueReleases = new Vector<String>();
                Map<String, String> issueReleaseMap = new HashMap<String, String>();
                RemoteVersion[] fixVersions = remoteIssue.getFixVersions();
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
                            warnings.add(new JiraVersionMappingWarning(jiraURL, issueKey, remoteVersionName,
                                    release));
                            releases.put(remoteVersionName, release);
                        }
                        issueReleaseMap.put(release, release);
                    }
                }
                for (String release : issueReleaseMap.keySet()) {
                    issueReleases.add(release);
                }
                if (issueReleases.size() > 1) {
                    warnings.add(new JiraIssueWithMultipleReleasesWarning(jiraURL, issueKey, issueReleases));
                }
                String typeId = remoteIssue.getType();
                String typeName = issueTypes.get(typeId);
                if (typeName == null) {
                    typeName = config.getProperty("jira.type.map.id." + typeId, "housekeeping");
                    warnings.add(new JiraIssueTypeMappingWarning(jiraURL, issueKey, typeId, typeName));
                    issueTypes.put(typeId, typeName);
                }
                issueData = new IssueData(issueKey, typeName, remoteIssue.getSummary(), null);
                parentIssues.put(issueKey, issueData);
            }
        }
        if (issueData != null && subTaskKey != null) {
            issueData.putSubTaskKey(subTaskKey);
        }
        return issueData;
    }
}
