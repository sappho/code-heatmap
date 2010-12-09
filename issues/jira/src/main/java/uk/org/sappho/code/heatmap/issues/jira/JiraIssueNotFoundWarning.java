package uk.org.sappho.code.heatmap.issues.jira;

public class JiraIssueNotFoundWarning extends JiraWarning {

    public JiraIssueNotFoundWarning(String baseURL, String issueKey) {

        super(baseURL, issueKey);
    }

    @Override
    public String getCategory() {

        return "Issue not found";
    }

    @Override
    public String toString() {

        return "Jira issue " + getIssueKey()
                + " not found - query specified in jira.jql.issues.allowed configuration too restrictive?";
    }
}
