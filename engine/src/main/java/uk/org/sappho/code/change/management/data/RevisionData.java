package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import uk.org.sappho.string.mapping.Mapper;

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
    private String commitComment;
    @NotNull
    @NotEmpty
    private String committer;
    private boolean isMerge = false;
    @NotNull
    @NotEmpty
    private List<String> changedFiles;
    @NotNull
    private List<String> badPaths;

    public RevisionData() {
    }

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

    public void setIssueKey(Mapper commitCommentToIssueKeyMapper) {

        issueKey = commitCommentToIssueKeyMapper.map(commitComment);
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
