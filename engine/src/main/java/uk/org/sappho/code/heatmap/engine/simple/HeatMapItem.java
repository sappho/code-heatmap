package uk.org.sappho.code.heatmap.engine.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import uk.org.sappho.code.change.management.data.ChangeSet;
import uk.org.sappho.code.change.management.data.IssueData;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String configurableItemName;
    private final Map<IssueData, List<ChangeSet>> issues = new HashMap<IssueData, List<ChangeSet>>();

    public HeatMapItem(String configurableItemName) {

        this.configurableItemName = configurableItemName;
    }

    public void add(ChangeSet changeSet) {

        IssueData issue = changeSet.getIssue();
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

    public Set<IssueData> getIssues() {

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
        for (IssueData issue : issues.keySet()) {
            weight += issue.getWeight();
        }
        return weight;
    }

    public String getWeightFormula() {

        // TODO: make this into something less lame!
        String formula = "";
        String prefix = "";
        for (IssueData issue : issues.keySet()) {
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
