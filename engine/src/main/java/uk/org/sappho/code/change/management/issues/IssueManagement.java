package uk.org.sappho.code.change.management.issues;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.WarningList;

public interface IssueManagement {

    public void init(WarningList warnings) throws IssueManagementException;

    public IssueData getIssueData(String issueKey);

    public List<String> getRawReleases();
}
