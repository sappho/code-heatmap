package uk.org.sappho.code.heatmap.issues.jira;

public class JiraSubTaskMappingWarning extends JiraWarning {

    private final String subTaskKey;
    private final String parentIssueKey;

    public JiraSubTaskMappingWarning(String jiraURL, String subTaskKey, String parentIssueKey) {

        super(jiraURL);
        this.subTaskKey = subTaskKey;
        this.parentIssueKey = parentIssueKey;
    }

    @Override
    public String getTypeName() {

        return "Issue subtask mapping";
    }

    @Override
    public String toString() {

        return "subtask " + subTaskKey + " --> issue " + parentIssueKey;
    }
}
