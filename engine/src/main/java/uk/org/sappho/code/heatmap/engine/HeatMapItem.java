package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.org.sappho.code.heatmap.issues.Issue;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String name;
    private final Map<Issue, List<Change>> issues = new HashMap<Issue, List<Change>>();
    private static final Logger LOG = Logger.getLogger(HeatMapItem.class);

    public HeatMapItem(String name) {

        this.name = name;
    }

    public void update(Change change) throws IssueManagementException {

        Issue issue = change.getIssue();
        List<Change> changes = issues.get(issue);
        if (changes == null) {
            changes = new Vector<Change>();
            issues.put(issue, changes);
        }
        changes.add(change);
    }

    public String getName() {

        return name;
    }

    public Set<Issue> getIssues() {

        return issues.keySet();
    }

    public int getIssueCount() {

        return issues.size();
    }

    public int getChangeCount() {

        int count = 0;
        for (List<Change> changes : issues.values()) {
            count += changes.size();
        }
        return count;
    }

    public int getWeight() throws IssueManagementException {

        int weight = 0;
        for (Issue issue : getIssues()) {
            weight += issue.getWeight();
        }
        return weight;
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

    @Override
    public String toString() {

        String str = name + " - " + getIssueCount() + " jira(s) and " + getChangeCount() + " change(s)\n   ";
        for (Issue issue : getIssues()) {
            try {
                str += " " + issue.getId();
            } catch (IssueManagementException e) {
                LOG.error("Issue management error", e);
            }
        }
        return str;
    }
}
