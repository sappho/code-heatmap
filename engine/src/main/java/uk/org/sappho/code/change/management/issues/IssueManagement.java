package uk.org.sappho.code.change.management.issues;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.Warnings;
import uk.org.sappho.configuration.ConfigurationException;

public interface IssueManagement {

    public void init(Warnings warnings) throws IssueManagementException, ConfigurationException;

    public IssueData getIssueData(String issueKey);

    public List<String> getRawReleases();
}
