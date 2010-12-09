package uk.org.sappho.code.heatmap.issues.jira;

import uk.org.sappho.warnings.Warning;

public abstract class JiraWarning extends Warning {

    private final String baseURL;
    private final String issueKey;

    public JiraWarning(String baseURL, String key) {

        this.baseURL = baseURL;
        this.issueKey = key;
    }

    public String getIssueURL() {

        return baseURL + "/browse/" + issueKey;
    }

    public String getIssueKey() {

        return issueKey;
    }
}
