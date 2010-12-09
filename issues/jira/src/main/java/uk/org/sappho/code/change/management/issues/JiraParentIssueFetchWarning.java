package uk.org.sappho.code.change.management.issues;

public class JiraParentIssueFetchWarning extends JiraWarning {

    public JiraParentIssueFetchWarning(String baseURL, String issueKey) {

        super(baseURL, issueKey);
    }

    @Override
    public String getCategory() {

        return "Parent issue fetch";
    }

    @Override
    public String toString() {

        return "Unable to determine if " + getIssueKey() + " has a parent issue";
    }
}
