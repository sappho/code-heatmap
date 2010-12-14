package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

public class RevisionData {

    private final String revisionKey;
    private String issueKey = null;
    private final Date date;
    private final String commitComment;
    private final String committer;
    private final List<String> changedFiles;
    private final List<String> badPaths;

    public RevisionData(String revisionKey, Date date, String commitComment, String committer,
            List<String> changedFiles, List<String> badPaths) {

        this.revisionKey = revisionKey;
        this.date = date;
        this.commitComment = commitComment;
        this.committer = committer;
        this.changedFiles = changedFiles;
        this.badPaths = badPaths;
    }

    public final String getKey() {

        return revisionKey;
    }

    public final Date getDate() {

        return date;
    }

    public final String getCommitter() {

        return committer;
    }

    public final String getCommitComment() {

        return commitComment;
    }

    public final List<String> getChangedFiles() {

        return changedFiles;
    }

    public final List<String> getBadPaths() {

        return badPaths;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getIssueKey() {
        return issueKey;
    }
}
