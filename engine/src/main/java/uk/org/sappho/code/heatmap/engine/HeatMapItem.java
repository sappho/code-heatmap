package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String name;
    private final Map<String, List<Change>> issues = new HashMap<String, List<Change>>();

    public HeatMapItem(String name) {

        this.name = name;
    }

    public void update(Change change) throws IssueManagementException {

        String jiraId = change.getIssue().getId();
        List<Change> jira = issues.get(jiraId);
        if (jira == null) {
            jira = new Vector<Change>();
            issues.put(jiraId, jira);
        }
        jira.add(change);
    }

    public String getName() {

        return name;
    }

    public Set<String> getIssues() {

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

    public int compareTo(HeatMapItem other) {

        int weight = other.getIssueCount() - getIssueCount();
        return weight == 0 ? other.getChangeCount() - getChangeCount() : weight;
    }

    @Override
    public String toString() {

        String str = name + " - " + getIssueCount() + " jira(s) and " + getChangeCount() + " change(s)\n   ";
        for (String jiraId : issues.keySet()) {
            str += " " + jiraId;
        }
        return str;
    }
}
