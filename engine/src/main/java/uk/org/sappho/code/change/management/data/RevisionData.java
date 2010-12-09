package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

public class RevisionData {

    private final String revisionKey;
    private final Date date;
    private final String commitComment;
    private final List<String> changedFiles;

    public RevisionData(String revisionKey, Date date, String commitComment, List<String> changedFiles) {

        this.revisionKey = revisionKey;
        this.date = date;
        this.commitComment = commitComment;
        this.changedFiles = changedFiles;
    }

    public final String getKey() {

        return revisionKey;
    }

    public final Date getDate() {

        return date;
    }

    public final String getCommitComment() {

        return commitComment;
    }

    public final List<String> getChangedFiles() {

        return changedFiles;
    }
}
