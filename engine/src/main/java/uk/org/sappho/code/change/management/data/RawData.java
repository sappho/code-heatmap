package uk.org.sappho.code.change.management.data;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.closure;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.var;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.lambdaj.function.closure.Closure;
import ch.lambdaj.group.Group;

import uk.org.sappho.string.mapping.Mapper;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.WarningList;

public class RawData implements Comparable<RawData>, Validatable {

    private Map<String, RevisionData> revisionDataMap = new HashMap<String, RevisionData>();
    private Map<String, IssueData> issueDataMap = new HashMap<String, IssueData>();
    private Map<String, String> issueKeyToIssueKeyMap = new HashMap<String, String>();
    private final WarningList warningList = new SimpleWarningList();

    public void reWire(Mapper commitCommentToIssueKeyMapper) {

        issueKeyToIssueKeyMap = new HashMap<String, String>();
        Group<String> x = group(issueDataMap.keySet(), by(on(String.class)));
        if (x != null) {
            x = null;
        }
        for (String issueKey : issueDataMap.keySet()) {
            IssueData issueData = issueDataMap.get(issueKey);
            issueKeyToIssueKeyMap.put(issueKey, issueKey);
            for (String subTaskKey : issueData.getSubTaskKeys()) {
                issueKeyToIssueKeyMap.put(subTaskKey, issueKey);
            }
        }
        Closure setIssueKey = closure();
        {
            RevisionData revisionData = var(RevisionData.class);
            String commitComment = revisionData.getCommitComment();
            String issueKey = commitCommentToIssueKeyMapper.map(commitComment);
            revisionData.setIssueKey(issueKey);
        }
        setIssueKey.each(revisionDataMap.values());
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

    public void putWarnings(WarningList warningList) {

        for (String category : warningList.getCategories()) {
            for (String warning : warningList.getWarnings(category)) {
                this.warningList.add(category, warning, false);
            }
        }
    }

    public WarningList getWarnings() {

        return warningList;
    }

    public Map<String, IssueData> getIssueDataMap() {
        return issueDataMap;
    }

    public int compareTo(RawData o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void checkValidity() throws ValidationException {
        // TODO Auto-generated method stub

    }
}
