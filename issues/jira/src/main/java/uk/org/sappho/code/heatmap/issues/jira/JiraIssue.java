package uk.org.sappho.code.heatmap.issues.jira;

import uk.org.sappho.code.heatmap.issues.Issue;

public class JiraIssue implements Issue {

    private final org.codehaus.swizzle.jira.Issue issue;
    private int weight;

    public JiraIssue(org.codehaus.swizzle.jira.Issue issue, int weight) {

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
