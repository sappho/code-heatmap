package uk.org.sappho.code.change.management.data;

import static ch.lambdaj.Lambda.forEach;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.oval.Validator;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.NotNull;

import uk.org.sappho.code.change.management.data.validation.IssueKeyMapConstraint;
import uk.org.sappho.string.mapping.Mapper;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.WarningList;

public class RawData {

    @NotNull
    private Map<String, RevisionData> revisionDataMap = new HashMap<String, RevisionData>();
    @NotNull
    private Map<String, IssueData> issueDataMap = new HashMap<String, IssueData>();
    @IssueKeyMapConstraint
    private Map<String, String> issueKeyToIssueKeyMap = new HashMap<String, String>();
    @NotNull
    @AssertValid
    private WarningList warningList = new SimpleWarningList();

    public void reWire(Mapper commitCommentToIssueKeyMapper) {

        issueKeyToIssueKeyMap = new HashMap<String, String>();
        for (String issueKey : issueDataMap.keySet()) {
            IssueData issueData = issueDataMap.get(issueKey);
            issueKeyToIssueKeyMap.put(issueKey, issueKey);
            for (String subTaskKey : issueData.getSubTaskKeys()) {
                issueKeyToIssueKeyMap.put(subTaskKey, issueKey);
            }
        }
        forEach(revisionDataMap.values()).setIssueKey(commitCommentToIssueKeyMapper);
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

        if (warningList == null)
            warningList = new SimpleWarningList();
        return warningList;
    }

    public Map<String, IssueData> getIssueDataMap() {
        return issueDataMap;
    }

    public boolean isValid() {

        Validator validator = new Validator();
        boolean valid = validator.validate(this).size() == 0;
        if (!valid) {
            if (warningList == null)
                warningList = new SimpleWarningList();
            warningList.add("Invalid raw data",
                    "Elements of raw data are missing or invalid - see other warnings if there are any");
        }
        return valid;
    }
}
