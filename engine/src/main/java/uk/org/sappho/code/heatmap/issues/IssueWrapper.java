package uk.org.sappho.code.heatmap.issues;

public interface IssueWrapper {

    public String getId() throws IssueManagementException;

    public String getTypeName() throws IssueManagementException;

    public String getSummary() throws IssueManagementException;

    public int getWeight() throws IssueManagementException;
}
