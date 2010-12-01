package uk.org.sappho.code.heatmap.issues.jira;

public class JiraIssueTypeMappingWarning extends JiraWarning {

    private final String rawIssueType;
    private final String issueType;

    public JiraIssueTypeMappingWarning(String jiraURL, String rawIssueType, String issueType) {

        super(jiraURL);
        this.rawIssueType = rawIssueType;
        this.issueType = issueType;
    }

    @Override
    public String getTypeName() {

        return "Issue type mapping";
    }

    @Override
    public String toString() {

        return "raw issue type " + rawIssueType + " --> issue type " + issueType;
    }
}
