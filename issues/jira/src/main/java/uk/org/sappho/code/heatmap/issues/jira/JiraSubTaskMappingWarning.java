package uk.org.sappho.code.heatmap.issues.jira;

public class JiraSubTaskMappingWarning extends JiraWarning {

    private final String parentIssueKey;

    public JiraSubTaskMappingWarning(String baseURL, String subTaskKey, String parentIssueKey) {

        super(baseURL, subTaskKey);
        this.parentIssueKey = parentIssueKey;
    }

    public String getParentIssueKey() {
        return parentIssueKey;
    }

    @Override
    public String getCategory() {

        return "Issue subtask mapping";
    }

    @Override
    public String toString() {

        return "subtask " + getIssueKey() + " --> issue " + parentIssueKey;
    }
}
