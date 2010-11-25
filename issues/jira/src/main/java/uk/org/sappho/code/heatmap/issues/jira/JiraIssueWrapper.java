package uk.org.sappho.code.heatmap.issues.jira;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;

import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class JiraIssueWrapper implements IssueWrapper {

    private final RemoteIssue issue;
    private final int weight;

    public JiraIssueWrapper(RemoteIssue issue, int weight) {

        this.issue = issue;
        this.weight = weight;
    }

    public String getId() {

        return issue.getKey();
    }

    public String getSummary() {

        return issue.getSummary();
    }

    public int getWeight() {

        return weight;
    }
}
