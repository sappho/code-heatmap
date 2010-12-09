package uk.org.sappho.code.change.management.issues;

import java.util.List;

public class JiraIssueWithMultipleReleasesWarning extends JiraWarning {

    private final List<String> releases;

    public JiraIssueWithMultipleReleasesWarning(String baseURL, String issueKey, List<String> releases) {

        super(baseURL, issueKey);
        this.releases = releases;
    }

    @Override
    public String getCategory() {

        return "Multiple releases";
    }

    @Override
    public String toString() {

        String warning = "issue " + getIssueKey() + " has multiple releases:";
        for (String release : releases) {
            warning += " " + release;
        }
        return warning;
    }
}
