package uk.org.sappho.code.heatmap.issues.jira;

import java.util.HashMap;
import java.util.Map;
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
    protected Map<String, Integer> issueTypeWeightMultipliers = new HashMap<String, Integer>();
    protected Configuration config;
    protected static final Pattern SIMPLE_JIRA_REGEX = Pattern.compile("^([A-Z]+-[0-9]+):.*$");
    private static final Logger LOG = Logger.getLogger(JiraService.class);

    @Inject
    public JiraService(Configuration config) throws IssueManagementException {

        LOG.info("Using Jira issue management plugin");
        this.config = config;
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
                    issue = createIssueWrapper(swizzleIssue);
                }
            } catch (Throwable t) {
                LOG.debug("Jira issue " + id + " not found or unable to work out its weight", t);
            }
        }
        return issue;
    }

    protected Issue createIssueWrapper(org.codehaus.swizzle.jira.Issue swizzleIssue) throws IssueManagementException {

        String typeName = swizzleIssue.getType().getName();
        Integer weight = issueTypeWeightMultipliers.get(typeName);
        if (weight == null) {
            String typeNameKey = "jira.type.multiplier." + typeName;
            try {
                weight = Integer.parseInt(config.getProperty(typeNameKey, "1"));
                LOG.info("Weight of issue type " + typeName + " is " + weight);
            } catch (Throwable t) {
                throw new IssueManagementException(
                        "Issue type weight configuration \"" + typeNameKey + "\" is invalid", t);
            }
            issueTypeWeightMultipliers.put(typeName, weight);
        }
        return new JiraIssueWrapper(swizzleIssue, weight);
    }

    protected String getIssueIdFromCommitComment(String commitComment) {

        String id = null;
        Matcher matcher = SIMPLE_JIRA_REGEX.matcher(commitComment.split("\n")[0]);
        if (matcher.matches()) {
            id = matcher.group(1);
        } else {
            LOG.debug("No issue ID found in commit comment: " + commitComment);
        }
        return id;
    }
}
