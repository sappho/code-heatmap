package uk.org.sappho.code.heatmap.issues.jira;

public class JiraIssueTypeWeightWarning extends JiraWarning {

    private final String issueType;
    private final int weight;

    public JiraIssueTypeWeightWarning(String jiraURL, String issueType, int weight) {

        super(jiraURL);
        this.issueType = issueType;
        this.weight = weight;
    }

    @Override
    public String getTypeName() {

        return "Issue type weight";
    }

    @Override
    public String toString() {

        return issueType + " = " + weight;
    }

}
