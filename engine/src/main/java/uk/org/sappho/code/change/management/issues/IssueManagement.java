package uk.org.sappho.code.change.management.issues;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;

public interface IssueManagement {

    public IssueData getIssueData(String issueKey);

    public List<String> getRawReleases();
}
