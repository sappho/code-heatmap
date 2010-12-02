package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String configurableItemName;
    private final Map<IssueWrapper, List<ChangeSet>> issues = new HashMap<IssueWrapper, List<ChangeSet>>();

    public HeatMapItem(String configurableItemName) {

        this.configurableItemName = configurableItemName;
    }

    public void update(ChangeSet changeSet) throws IssueManagementException {

        IssueWrapper issue = changeSet.getIssue();
        List<ChangeSet> changes = issues.get(issue);
        if (changes == null) {
            changes = new Vector<ChangeSet>();
            issues.put(issue, changes);
        }
        changes.add(changeSet);
    }

    public String getHeatMapItemName() {

        return configurableItemName;
    }

    public Set<IssueWrapper> getIssues() {

        return issues.keySet();
    }

    public int getIssuesCount() {

        return issues.keySet().size();
    }

    public int getChangeCount() {

        int count = 0;
        for (List<ChangeSet> changes : issues.values()) {
            count += changes.size();
        }
        return count;
    }

    public int getWeight() {

        int weight = 0;
        for (IssueWrapper issue : issues.keySet()) {
            weight += issue.getWeight();
        }
        return weight;
    }

    public String getWeightFormula() {

        // TODO: make this into something less lame!
        String formula = "";
        String prefix = "";
        for (IssueWrapper issue : issues.keySet()) {
            formula += prefix + issue.getKey() + ":" + issue.getWeight();
            prefix = " + ";
        }
        return formula;
    }

    public int compareTo(HeatMapItem other) {

        int comparison = 0;
        comparison = other.getWeight() - getWeight();
        return comparison;
    }
}
