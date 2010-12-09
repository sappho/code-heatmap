package uk.org.sappho.code.change.management.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RawData {

    private Map<String, RevisionData> revisionDataMap = new HashMap<String, RevisionData>();
    private Map<String, IssueData> issueDataMap = new HashMap<String, IssueData>();

    public void clearRevisionData() {

        revisionDataMap = new HashMap<String, RevisionData>();
    }

    public void putRevisionData(RevisionData revisionData) {

        revisionDataMap.put(revisionData.getKey(), revisionData);
    }

    public Set<String> getRevisionKeys() {

        return revisionDataMap.keySet();
    }

    public RevisionData getRevisionData(String revisionKey) {

        return revisionDataMap.get(revisionKey);
    }

    public void clearIssueData() {

        issueDataMap = new HashMap<String, IssueData>();
    }

    public void putIssueData(IssueData issueData) {

        issueDataMap.put(issueData.getKey(), issueData);
    }

    public Set<String> getIssueKeys() {

        return issueDataMap.keySet();
    }

    public IssueData getIssueData(String issueKey) {

        return issueDataMap.get(issueKey);
    }
}
