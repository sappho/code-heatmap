package uk.org.sappho.code.change.management.data.mapping;

public interface CommitCommentToIssueKeyMapper {

    public String getIssueKeyFromCommitComment(String revisionKey, String commitComment);
}
