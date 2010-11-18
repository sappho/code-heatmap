package uk.org.sappho.code.heatmap.issues;

public interface IssueManagement {

    public Issue getIssue(String id) throws IssueManagementException;
}
