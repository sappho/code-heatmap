package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import uk.org.sappho.validation.StringArrayConstraint;

public class RevisionData {

    @NotNull
    @NotEmpty
    private String revisionKey;
    @NotNull
    @NotEmpty
    private String issueKey;
    @NotNull
    @NotEmpty
    private Date date;
    @NotNull
    @NotEmpty
    private String commitComment;
    @NotNull
    @NotEmpty
    private String committer;
    @NotNull
    @NotEmpty
    private Boolean isMerge = false;
    @StringArrayConstraint
    private List<String> changedFiles;

    public RevisionData() {
    }

    public RevisionData(String revisionKey, Date date, String commitComment, String committer,
            List<String> changedFiles) {

        this.revisionKey = revisionKey;
        this.date = date;
        this.commitComment = commitComment;
        this.committer = committer;
        this.changedFiles = changedFiles;
    }

    public final String getRevisionKey() {

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

    public void setIssueKey(String issueKey) {

        this.issueKey = issueKey;
    }

    public String getIssueKey() {

        return issueKey;
    }

    public void setMerge(boolean isMerge) {

        this.isMerge = isMerge;
    }

    public boolean isMerge() {

        return isMerge;
    }

    @Override
    public String toString() {

        return "revision " + revisionKey + " committed on " + date;
    }
}
