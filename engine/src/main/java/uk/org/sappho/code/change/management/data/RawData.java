package uk.org.sappho.code.change.management.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.org.sappho.code.change.management.data.mapping.CommitCommentToIssueKeyMapper;

public class RawData {

    private Map<String, RevisionData> revisionDataMap = new HashMap<String, RevisionData>();
    private Map<String, IssueData> issueDataMap = new HashMap<String, IssueData>();
    private Map<String, String> issueKeyToIssueKeyMap = new HashMap<String, String>();

    public void reWire(CommitCommentToIssueKeyMapper commitCommentToIssueKeyMapper) {

        issueKeyToIssueKeyMap = new HashMap<String, String>();
        for (String issueKey : issueDataMap.keySet()) {
            IssueData issueData = issueDataMap.get(issueKey);
            issueKeyToIssueKeyMap.put(issueKey, issueKey);
            for (String subTaskKey : issueData.getSubTaskKeys()) {
                issueKeyToIssueKeyMap.put(subTaskKey, issueKey);
            }
        }
        for (String revisionKey : revisionDataMap.keySet()) {
            RevisionData revisionData = revisionDataMap.get(revisionKey);
            String commitComment = revisionData.getCommitComment();
            String issueKey = commitCommentToIssueKeyMapper.getIssueKeyFromCommitComment(revisionKey, commitComment);
            revisionData.setIssueKey(issueKey);
        }
    }

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
        issueKeyToIssueKeyMap = new HashMap<String, String>();
    }

    public void putIssueData(IssueData issueData) {

        issueDataMap.put(issueData.getKey(), issueData);
    }

    public Set<String> getIssueKeys() {

        return issueDataMap.keySet();
    }

    public IssueData getIssueData(String issueKey) {

        return issueDataMap.get(issueKeyToIssueKeyMap.get(issueKey));
    }
}
