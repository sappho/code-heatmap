package uk.org.sappho.code.change.management.issues;

import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;

public interface IssueManagement {

    public IssueData getIssueData(String issueKey);

    public Map<String, String> getReleaseMappings();

    public Map<String, String> getIssueTypeMappings();
}
