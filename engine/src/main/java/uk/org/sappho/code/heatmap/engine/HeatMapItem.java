package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String name;
    private final Map<IssueWrapper, List<ChangeSet>> issues = new HashMap<IssueWrapper, List<ChangeSet>>();
    private final Map<IssueWrapper, List<ChangeSet>> parentIssues = new HashMap<IssueWrapper, List<ChangeSet>>();
    private final Map<String, IssueWrapper> mappedParentIssues = new HashMap<String, IssueWrapper>();
    private static final Logger LOG = Logger.getLogger(HeatMapItem.class);

    public HeatMapItem(String name) {

        this.name = name;
    }

    public void update(ChangeSet change) throws IssueManagementException {

        IssueWrapper issue = change.getIssue();
        String issueKey = issue.getKey();
        List<ChangeSet> changes = issues.get(issue);
        if (changes == null) {
            changes = new Vector<ChangeSet>();
            issues.put(issue, changes);
        }
        changes.add(change);
        IssueWrapper parentIssue = mappedParentIssues.get(issueKey);
        if (parentIssue != null) {
            issue = parentIssue;
        } else {
            mappedParentIssues.put(issueKey, issue);
        }
        changes = parentIssues.get(issue);
        if (changes == null) {
            changes = new Vector<ChangeSet>();
            parentIssues.put(issue, changes);
        }
        changes.add(change);
    }

    public String getName() {

        return name;
    }

    public Set<IssueWrapper> getIssues() {

        return issues.keySet();
    }

    public Set<IssueWrapper> getParentIssues() {

        return parentIssues.keySet();
    }

    public int getChangeCount() {

        int count = 0;
        for (List<ChangeSet> changes : issues.values()) {
            count += changes.size();
        }
        return count;
    }

    public int getWeight() throws IssueManagementException {

        int weight = 0;
        for (IssueWrapper issue : getParentIssues()) {
            weight += issue.getWeight();
        }
        return weight;
    }

    public String getWeightFormula() throws IssueManagementException {

        String formula = "";
        String prefix = "";
        for (IssueWrapper issue : getParentIssues()) {
            formula += prefix + issue.getKey() + ":" + issue.getWeight();
            prefix = " + ";
        }
        return formula;
    }

    public int compareTo(HeatMapItem other) {

        int comparison = 0;
        try {
            comparison = other.getWeight() - getWeight();
        } catch (IssueManagementException e) {
            LOG.error("Issue management error", e);
        }
        return comparison;
    }
}
