package uk.org.sappho.code.heatmap.issues.jira;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected final Jira jira;
    protected static final Pattern SIMPLE_JIRA_REGEX = Pattern.compile("^([A-Z]+-[0-9]+):.*$");
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

    public Issue getIssue(String commitComment) {

        Issue issue = null;
        String id = getIssueIdFromCommitComment(commitComment);
        if (id != null) {
            try {
                org.codehaus.swizzle.jira.Issue swizzleIssue = jira.getIssue(id);
                if (swizzleIssue != null) {
                    issue = new JiraIssue(swizzleIssue);
                }
            } catch (Throwable t) {
                LOG.debug("Jira issue " + id + " not found", t);
            }
        }
        return issue;
    }

    protected String getIssueIdFromCommitComment(String commitComment) {

        String id = null;
        Matcher matcher = SIMPLE_JIRA_REGEX.matcher(commitComment.split("\n")[0]);
        if (matcher.matches()) {
            id = matcher.group(1);
        } else {
            LOG.debug("No isue ID found in commit comment: " + commitComment);
        }
        return id;
    }
}
