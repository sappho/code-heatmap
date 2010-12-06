package uk.org.sappho.code.heatmap.issues.jira;

import java.util.List;

public class JiraIssueWithMultipleReleasesWarning extends JiraWarning {

    private final String issueKey;
    private final List<String> releases;

    public JiraIssueWithMultipleReleasesWarning(String jiraURL, String issueKey, List<String> releases) {

        super(jiraURL);
        this.issueKey = issueKey;
        this.releases = releases;
    }

    @Override
    public String getTypeName() {

        return "Multiple releases";
    }

    @Override
    public String toString() {

        String warning = "issue " + issueKey + " has multiple releases:";
        for (String release : releases) {
            warning += " " + release;
        }
        return warning;
    }
}
