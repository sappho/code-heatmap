package uk.org.sappho.code.change.management.issues;

public class JiraVersionMappingWarning extends JiraWarning {

    private final String version;
    private final String release;

    public JiraVersionMappingWarning(String baseURL, String issueKey, String version, String release) {

        super(baseURL, issueKey);
        this.version = version;
        this.release = release;
    }

    public String getVersion() {
        return version;
    }

    public String getRelease() {
        return release;
    }

    @Override
    public String getCategory() {

        return "Version mapping";
    }

    @Override
    public String toString() {

        return "version " + version + " --> release " + release;
    }
}
