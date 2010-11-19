package uk.org.sappho.code.heatmap.issues;

public interface IssueManagement {

    public Issue getIssue(String commitComment);

    public int getIssueTypeWeightMultiplier(Issue issue) throws IssueManagementException;
}
