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
import com.atlassian.jira.rpc.soap.client.RemoteResolution;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;
import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.WarningList;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.jira4j.soap.JiraSoapService;

public class Jira implements IssueManagement {

    private final Configuration config;
    private WarningList warnings = null;
    private String jiraURL = null;
    private JiraSoapService jiraSoapService = null;
    private SapphoJiraRpcSoapServiceWrapper sapphoJiraRpcSoapServiceWrapper = null;
    private final Map<String, String> mappedRemoteIssueTypes = new HashMap<String, String>();
    private final Map<String, String> mappedRemoteResolutions = new HashMap<String, String>();
    private final Map<String, IssueData> issueMappings = new HashMap<String, IssueData>();
    private final Map<String, String> movedIssueMappings = new HashMap<String, String>();
    private final Map<String, String> parentIssueMappings = new HashMap<String, String>();
    private final List<String> allRawReleases = new ArrayList<String>();
    private static final Logger log = Logger.getLogger(Jira.class);

    @Inject
    public Jira(Configuration config) {

        log.info("Using Jira issue management plugin");
        this.config = config;
    }

    public void init(WarningList warnings) throws IssueManagementException, ConfigurationException {

        this.warnings = warnings;
        jiraURL = config.getProperty("jira.url");
        String username = config.getProperty("jira.username");
        String password = config.getProperty("jira.password");
        log.info("Connecting to " + jiraURL + " as " + username);
        try {
            jiraSoapService = new JiraSoapService(jiraURL, username, password);
            sapphoJiraRpcSoapServiceWrapper = new SapphoJiraRpcSoapServiceWrapper(jiraURL, username, password);
            RemoteIssueType[] remoteIssueTypes = jiraSoapService.getService().getIssueTypes(jiraSoapService.getToken());
            for (RemoteIssueType remoteIssueType : remoteIssueTypes)
                mappedRemoteIssueTypes.put(remoteIssueType.getId(), remoteIssueType.getName());
            RemoteResolution[] remoteResolutions = jiraSoapService.getService().getResolutions(
                    jiraSoapService.getToken());
            for (RemoteResolution remoteResolution : remoteResolutions)
                mappedRemoteResolutions.put(remoteResolution.getId(), remoteResolution.getName());
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to connect to " + jiraURL + " as user " + username
                    + " - is Sappho SOAP service installed?", t);
        }
    }

    private String getRealIssueKey(String issueKey) {

        String newIssueKey = movedIssueMappings.get(issueKey);
        if (newIssueKey == null) {
            try {
                newIssueKey = sapphoJiraRpcSoapServiceWrapper.getMovedIssueKey(issueKey);
                if (newIssueKey != null)
                    warnings.add("Moved issue", "Issue " + issueKey + " has moved to " + newIssueKey);
            } catch (Exception e) {
                // if this doesn't work it'll be caught further downstream so can be ignored here
            }
            if (newIssueKey == null)
                newIssueKey = issueKey;
            movedIssueMappings.put(issueKey, newIssueKey);
        }
        return newIssueKey;
    }

    private String getParentIssueKey(String issueKey) {

        String newIssueKey = parentIssueMappings.get(issueKey);
        if (newIssueKey == null) {
            try {
                newIssueKey = sapphoJiraRpcSoapServiceWrapper.getParent(issueKey);
                if (newIssueKey != null)
                    warnings.add("Subtask", "Issue " + issueKey + " is a subtask of " + newIssueKey);
            } catch (Exception e) {
                // if this doesn't work it'll be caught further downstream so can be ignored here
            }
            if (newIssueKey == null)
                newIssueKey = issueKey;
            parentIssueMappings.put(issueKey, newIssueKey);
        }
        return newIssueKey;
    }

    public IssueData getIssueData(String issueKey) {

        issueKey = getRealIssueKey(issueKey);
        issueKey = getParentIssueKey(issueKey);
        IssueData issueData = issueMappings.get(issueKey);
        if (issueData == null) {
            RemoteIssue remoteIssue = null;
            try {
                remoteIssue = jiraSoapService.getService().getIssue(jiraSoapService.getToken(), issueKey);
            } catch (Exception e) {
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
                String typeId = remoteIssue.getType();
                String type = mappedRemoteIssueTypes.get(typeId);
                RemoteComponent[] remoteComponents = remoteIssue.getComponents();
                List<String> components = new ArrayList<String>();
                for (RemoteComponent remoteComponent : remoteComponents) {
                    components.add(remoteComponent.getName());
                }
                String assignee = remoteIssue.getAssignee();
                String project = remoteIssue.getProject();
                String resolutionId = remoteIssue.getResolution();
                String resolution = mappedRemoteResolutions.get(resolutionId);
                Date createdOn = remoteIssue.getCreated().getTime();
                Date updatedOn = remoteIssue.getUpdated().getTime();
                issueData = new IssueData(issueKey, type, remoteIssue.getSummary(), createdOn, updatedOn,
                        assignee, project, resolution, components, issueRawReleases);
                issueMappings.put(issueKey, issueData);
            }
        }
        return issueData;
    }

    public List<String> getRawReleases() {

        return allRawReleases;
    }
}
