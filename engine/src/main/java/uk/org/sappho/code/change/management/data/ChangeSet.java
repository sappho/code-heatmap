package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

public class ChangeSet {

    private final String revisionId;
    private final Date date;
    private final String commitComment;
    private final IssueData issue;
    private final List<String> changedFiles;

    public ChangeSet(String revisionId, Date date, String commitComment, IssueData issue,
            List<String> changedFiles) {

        this.revisionId = revisionId;
        this.date = date;
        this.commitComment = commitComment;
        this.issue = issue;
        this.changedFiles = changedFiles;
    }

    public final String getRevisionId() {

        return revisionId;
    }

    public final Date getDate() {

        return date;
    }

    public final String getCommitComment() {

        return commitComment;
    }

    public final IssueData getIssue() {

        return issue;
    }

    public final List<String> getChangedFiles() {

        return changedFiles;
    }
}
