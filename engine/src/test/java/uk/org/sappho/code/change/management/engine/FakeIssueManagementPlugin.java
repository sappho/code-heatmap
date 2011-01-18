package uk.org.sappho.code.change.management.engine;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.Warnings;
import uk.org.sappho.code.change.management.issues.IssueManagement;
import uk.org.sappho.code.change.management.issues.IssueManagementException;

public class FakeIssueManagementPlugin implements IssueManagement {

    public FakeIssueManagementPlugin() {
    }

    public void init(Warnings warnings) throws IssueManagementException {
    }

    public IssueData getIssueData(String issueKey) {
        return null;
    }

    public List<String> getRawReleases() {
        return null;
    }
}
