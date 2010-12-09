package uk.org.sappho.code.change.management.issues;

public class JiraIssueTypeMappingWarning extends JiraWarning {

    private final String rawIssueType;
    private final String issueType;

    public JiraIssueTypeMappingWarning(String baseURL, String issueKey, String rawIssueType, String issueType) {

        super(baseURL, issueKey);
        this.rawIssueType = rawIssueType;
        this.issueType = issueType;
    }

    @Override
    public String getCategory() {

        return "Issue type mapping";
    }

    @Override
    public String toString() {

        return "Raw issue type " + rawIssueType + " --> issue type " + issueType;
    }
}
