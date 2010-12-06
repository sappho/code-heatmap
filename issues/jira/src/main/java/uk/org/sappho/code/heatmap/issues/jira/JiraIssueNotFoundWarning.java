package uk.org.sappho.code.heatmap.issues.jira;

public class JiraIssueNotFoundWarning extends JiraWarning {

    public String issueKey;

    public JiraIssueNotFoundWarning(String jiraURL, String issueKey) {

        super(jiraURL);
        this.issueKey = issueKey;
    }

    @Override
    public String getTypeName() {

        return "Issue not found";
    }

    @Override
    public String toString() {

        return "Jira issue " + issueKey
                + " not found - query specified in jira.jql.issues.allowed configuration too restrictive?";
    }
}
