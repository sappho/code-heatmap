package uk.org.sappho.code.heatmap.issues.jira;

import uk.org.sappho.code.heatmap.warnings.impl.Warning;

public abstract class JiraWarning extends Warning {

    protected final String jiraURL;

    public JiraWarning(String jiraURL) {

        this.jiraURL = jiraURL;
    }
}
