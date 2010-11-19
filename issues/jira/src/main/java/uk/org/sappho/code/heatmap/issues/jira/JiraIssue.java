package uk.org.sappho.code.heatmap.issues.jira;

import uk.org.sappho.code.heatmap.issues.Issue;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class JiraIssue implements Issue {

    private final org.codehaus.swizzle.jira.Issue issue;
    IssueManagement issueManagement;

    public JiraIssue(org.codehaus.swizzle.jira.Issue issue, IssueManagement issueManagement) {

        this.issue = issue;
        this.issueManagement = issueManagement;
    }

    public String getId() throws IssueManagementException {

        return issue.getKey();
    }

    public String getTypeName() throws IssueManagementException {

        return issue.getType().getName();
    }

    public String getSummary() throws IssueManagementException {

        return issue.getSummary();
    }

    public int getWeight() throws IssueManagementException {

        return issueManagement.getIssueTypeWeightMultiplier(this);
    }
}
