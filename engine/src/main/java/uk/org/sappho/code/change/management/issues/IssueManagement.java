package uk.org.sappho.code.change.management.issues;

import uk.org.sappho.code.change.management.data.IssueData;

public interface IssueManagement {

    public void init() throws IssueManagementException;

    public IssueData getIssue(String commitComment);
}
