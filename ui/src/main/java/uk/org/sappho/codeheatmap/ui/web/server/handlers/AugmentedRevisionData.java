package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import java.util.Date;
import java.util.List;

import uk.org.sappho.code.change.management.data.RevisionData;

public class AugmentedRevisionData extends RevisionData {

    private String mainRelease;
    private String type;

    public AugmentedRevisionData(String revisionKey, Date date, String commitComment, String committer,
            List<String> changedFiles, List<String> badPaths, String issueKey) {
        super(revisionKey, date, commitComment, committer, changedFiles, badPaths);
        setIssueKey(issueKey);
    }

    public void setMainRelease(String mainRelease) {
        this.mainRelease = mainRelease;
    }

    public String getMainRelease() {
        return mainRelease;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
