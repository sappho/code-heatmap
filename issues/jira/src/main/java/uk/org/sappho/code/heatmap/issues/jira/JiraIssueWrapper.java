package uk.org.sappho.code.heatmap.issues.jira;

import org.codehaus.swizzle.jira.Issue;

import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class JiraIssueWrapper implements IssueWrapper {

    private final Issue issue;
    private final int weight;

    public JiraIssueWrapper(Issue issue, int weight) {

        this.issue = issue;
        this.weight = weight;
    }

    public String getId() {

        return issue.getKey();
    }

    public String getTypeName() {

        return issue.getType().getName();
    }

    public String getSummary() {

        return issue.getSummary();
    }

    public int getWeight() {

        return weight;
    }
}
