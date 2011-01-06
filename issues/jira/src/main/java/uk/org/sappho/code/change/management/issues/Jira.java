package uk.org.sappho.code.change.management.issues;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sappho.jira.rpc.soap.client.SapphoJiraRpcSoapServiceWrapper;

import com.atlassian.jira.rpc.soap.client.RemoteComponent;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteIssueType;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;
import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.WarningList;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.jira4j.soap.JiraSoapService;

public class Jira implements IssueManagement {

    private final WarningList warnings;
    private String jiraURL = null;
    private JiraSoapService jiraSoapService = null;
    private SapphoJiraRpcSoapServiceWrapper sapphoJiraRpcSoapServiceWrapper = null;
    private final Map<String, String> mappedRemoteIssueTypes = new HashMap<String, String>();
    private final Map<String, RemoteIssue> mappedRemoteIssues = new HashMap<String, RemoteIssue>();
    private final Map<String, IssueData> parentIssues = new HashMap<String, IssueData>();
    private final Map<String, String> subTaskParents = new HashMap<String, String>();
    private final Map<String, String> movedIssueMappings = new HashMap<String, String>();
    private final List<String> allRawReleases = new ArrayList<String>();
    private static final Logger log = Logger.getLogger(Jira.class);

    @Inject
    public Jira(Configuration config, WarningList warnings) throws ConfigurationException,
            IssueManagementException {

        log.info("Using Jira issue management plugin");
        this.warnings = warnings;
        jiraURL = config.getProperty("jira.url", "http://example.com");
        String username = config.getProperty("jira.username", "nobody");
        String password = config.getProperty("jira.password", "nopassword");
        log.info("Connecting to " + jiraURL + " as " + username);
        try {
            jiraSoapService = new JiraSoapService(jiraURL, username, password);
            RemoteIssueType[] remoteIssueTypes = jiraSoapService.getService().getIssueTypes(jiraSoapService.getToken());
            for (RemoteIssueType remoteIssueType : remoteIssueTypes) {
                mappedRemoteIssueTypes.put(remoteIssueType.getId(), remoteIssueType.getName());
            }
            sapphoJiraRpcSoapServiceWrapper = new SapphoJiraRpcSoapServiceWrapper(jiraURL, username, password);
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to connect to " + jiraURL + " as user " + username
                    + " - is Sappho SOAP service installed?", t);
        }
    }

    private String getRealIssueKey(String issueKey) {

        String newIssueKey = movedIssueMappings.get(issueKey);
        if (newIssueKey == null) {
            if (sapphoJiraRpcSoapServiceWrapper != null) {
                try {
                    newIssueKey = sapphoJiraRpcSoapServiceWrapper.getMovedIssueKey(issueKey);
                    if (newIssueKey != null)
                        warnings.add("Moved issue", "Issue " + issueKey + " has moved to " + newIssueKey);
                } catch (Exception e) {
                    // if this doesn't work it'll be caught further downstream so can be ignored here
                }
            }
            if (newIssueKey == null) {
                newIssueKey = issueKey;
            }
            movedIssueMappings.put(issueKey, newIssueKey);
        }
        return newIssueKey;
    }

    public IssueData getIssueData(String issueKey) {

        String subTaskKey = null;
        issueKey = getRealIssueKey(issueKey);
        if (parentIssues.get(issueKey) == null) {
            String parentKey = subTaskParents.get(issueKey);
            if (parentKey == null) {
                if (sapphoJiraRpcSoapServiceWrapper != null) {
                    try {
                        parentKey = sapphoJiraRpcSoapServiceWrapper.getParent(issueKey);
                        if (parentKey != null) {
                            subTaskParents.put(issueKey, parentKey);
                        }
                    } catch (Exception e) {
                        warnings.add("Parent issue", "Unable to get parent issue of issue " + issueKey);
                    }
                }
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
                    if (remoteIssue != null) {
                        mappedRemoteIssues.put(issueKey, remoteIssue);
                    }
                } catch (Exception e) {
                }
            }
            if (remoteIssue != null) {
                List<String> issueRawReleases = new ArrayList<String>();
                RemoteVersion[] fixVersions = remoteIssue.getFixVersions();
                for (RemoteVersion remoteVersion : fixVersions) {
                    String remoteVersionName = remoteVersion.getName();
                    if (!issueRawReleases.contains(remoteVersionName)) {
                        issueRawReleases.add(remoteVersionName);
                    }
                    if (!allRawReleases.contains(remoteVersionName)) {
                        allRawReleases.add(remoteVersionName);
                    }
                }
                String rawTypeId = remoteIssue.getType();
                String rawTypeName = mappedRemoteIssueTypes.get(rawTypeId);
                RemoteComponent[] remoteComponents = remoteIssue.getComponents();
                List<String> components = new ArrayList<String>();
                for (RemoteComponent remoteComponent : remoteComponents) {
                    components.add(remoteComponent.getName());
                }
                String assignee = remoteIssue.getAssignee();
                String project = remoteIssue.getProject();
                Date createdOn = remoteIssue.getCreated().getTime();
                Date updatedOn = remoteIssue.getUpdated().getTime();
                issueData = new IssueData(issueKey, rawTypeName, remoteIssue.getSummary(), createdOn, updatedOn,
                        assignee, project, components, issueRawReleases);
                parentIssues.put(issueKey, issueData);
            }
        }
        if (issueData != null && subTaskKey != null) {
            issueData.putSubTaskKey(subTaskKey);
        }
        return issueData;
    }

    public List<String> getRawReleases() {

        return allRawReleases;
    }
}
