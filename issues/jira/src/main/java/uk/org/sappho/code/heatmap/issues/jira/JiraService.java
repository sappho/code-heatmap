package uk.org.sappho.code.heatmap.issues.jira;

import org.apache.log4j.Logger;
import org.codehaus.swizzle.jira.Jira;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.issues.Issue;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;

@Singleton
public class JiraService implements IssueManagement {

    private final Jira jira;
    private static final Logger LOG = Logger.getLogger(JiraService.class);

    @Inject
    public JiraService(Configuration config) throws IssueManagementException {

        LOG.info("Using Jira issue management plugin");
        String url = config.getProperty("jira.url", "http://example.com");
        String username = config.getProperty("jira.username", "nobody");
        LOG.info("Connecting to " + url + " as " + username);
        try {
            jira = new Jira(url);
            jira.login(username, config.getProperty("jira.password", "nopassword"));
        } catch (Throwable t) {
            throw new IssueManagementException("Unable to log in to Jira at " + url + " as user " + username, t);
        }
    }

    public Issue getIssue(String id) throws IssueManagementException {

        org.codehaus.swizzle.jira.Issue issue = null;
        try {
            issue = jira.getIssue(id);
        } catch (Throwable t) {
            throw new IssueManagementException("Jira issue " + id + " not found", t);
        }
        return new JiraIssue(issue);
    }
}
