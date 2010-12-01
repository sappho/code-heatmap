package uk.org.sappho.code.heatmap.issues.jira;


public class JiraVersionMappingWarning extends JiraWarning {

    private final String version;
    private final String release;

    public JiraVersionMappingWarning(String jiraURL, String version, String release) {

        super(jiraURL);
        this.version = version;
        this.release = release;
    }

    @Override
    public String getTypeName() {

        return "Version mapping";
    }

    @Override
    public String toString() {

        return "version " + version + " --> release " + release;
    }
}
