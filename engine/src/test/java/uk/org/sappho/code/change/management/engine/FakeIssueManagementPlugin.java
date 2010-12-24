package uk.org.sappho.code.change.management.engine;

import java.util.List;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.issues.IssueManagement;

public class FakeIssueManagementPlugin implements IssueManagement {

    @Inject
    public FakeIssueManagementPlugin() {
    }

    public IssueData getIssueData(String issueKey) {
        return null;
    }

    public List<String> getRawReleases() {
        return null;
    }
}
