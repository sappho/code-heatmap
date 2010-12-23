package uk.org.sappho.code.change.management.data;

import static ch.lambdaj.Lambda.forEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.NotNull;

import uk.org.sappho.code.change.management.data.validation.IssueKeyMappingConstraint;
import uk.org.sappho.string.mapping.Mapper;
import uk.org.sappho.validation.Validator;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.Warning;
import uk.org.sappho.warnings.WarningList;

public class RawData {

    @NotNull
    @AssertValid
    private Map<String, RevisionData> revisionDataMap = new HashMap<String, RevisionData>();
    @NotNull
    @AssertValid
    private Map<String, IssueData> issueDataMap = new HashMap<String, IssueData>();
    @IssueKeyMappingConstraint
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

        revisionDataMap.put(revisionData.getRevisionKey(), revisionData);
    }

    public Set<String> getRevisionKeys() {

        return revisionDataMap.keySet();
    }

    public RevisionData getRevisionData(String revisionKey) {

        return revisionDataMap.get(revisionKey);
    }

    public List<RevisionData> getRevisionsReferencingIssue(String issueKey) {

        List<RevisionData> revisions = new ArrayList<RevisionData>();
        for (RevisionData revisionData : revisionDataMap.values()) {
            String revisionIssueKey = revisionData.getIssueKey();
            if (revisionIssueKey != null && revisionIssueKey.equals(issueKey))
                revisions.add(revisionData);
        }
        return revisions;
    }

    public void clearIssueData() {

        issueDataMap = new HashMap<String, IssueData>();
        issueKeyToIssueKeyMap = new HashMap<String, String>();
    }

    public void putIssueData(IssueData issueData) {

        issueDataMap.put(issueData.getIssueKey(), issueData);
    }

    public Set<String> getIssueKeys() {

        return issueDataMap.keySet();
    }

    public IssueData getIssueData(String issueKey) {

        return issueDataMap.get(issueKeyToIssueKeyMap.get(issueKey));
    }

    public void putWarnings(WarningList warningList) {

        for (String category : warningList.getCategories()) {
            for (Warning warning : warningList.getWarnings(category)) {
                this.warningList.add(category, warning.getWarning(), false);
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

    public Map<String, RevisionData> getRevisionDataMap() {

        return revisionDataMap;
    }

    public boolean isValid() {

        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(this);
        boolean valid = violations.size() == 0;
        if (!valid)
            warningList.add("Invalid raw data", "Elements of raw data are invalid: " + validator.getReport(violations));
        return valid;
    }
}
