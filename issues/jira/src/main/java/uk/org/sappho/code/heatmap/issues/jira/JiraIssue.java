package uk.org.sappho.code.heatmap.issues.jira;

import uk.org.sappho.code.heatmap.issues.Issue;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class JiraIssue implements Issue {

    private final org.codehaus.swizzle.jira.Issue issue;

    public JiraIssue(org.codehaus.swizzle.jira.Issue issue) {

        this.issue = issue;
    }

    public String getId() throws IssueManagementException {

        return issue.getKey();
    }

    public String getSummary() throws IssueManagementException {

        return issue.getSummary();
    }

    public int getWeight() throws IssueManagementException {

        return 0;
    }

}
