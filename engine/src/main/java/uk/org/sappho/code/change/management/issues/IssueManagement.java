package uk.org.sappho.code.change.management.issues;

import uk.org.sappho.code.change.management.data.IssueData;

public interface IssueManagement {

    public IssueData getIssueData(String issueKey);
}
