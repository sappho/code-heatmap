package uk.org.sappho.code.change.management.issues;

import java.util.List;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;

public interface IssueManagement {

    public IssueData getIssueData(String issueKey);

    public List<String> getRawReleases();

    public Map<String, String> getIssueTypeMappings();
}
