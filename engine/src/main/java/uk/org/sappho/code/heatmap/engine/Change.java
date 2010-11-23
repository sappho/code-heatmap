package uk.org.sappho.code.heatmap.engine;

import java.util.List;

import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class Change {

    private final String revisionId;
    private final String commitComment;
    private final IssueWrapper issue;
    private final List<Filename> changedFiles;

    public Change(String revisionId, String commitComment, IssueWrapper issue, List<Filename> changedFiles) {

        this.revisionId = revisionId;
        this.commitComment = commitComment;
        this.issue = issue;
        this.changedFiles = changedFiles;
    }

    public final String getRevisionId() {

        return revisionId;
    }

    public final String getCommitComment() {

        return commitComment;
    }

    public final IssueWrapper getIssue() {

        return issue;
    }

    public final List<Filename> getChangedFiles() {

        return changedFiles;
    }
}
