package uk.org.sappho.code.heatmap.engine.simple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String configurableItemName;
    private final Map<IssueData, IssueData> issues = new HashMap<IssueData, IssueData>();
    private final Map<RevisionData, RevisionData> revisions = new HashMap<RevisionData, RevisionData>();

    public HeatMapItem(String configurableItemName) {

        this.configurableItemName = configurableItemName;
    }

    public void add(RevisionData revisionData, IssueData issueData) {

        issues.put(issueData, issueData);
        revisions.put(revisionData, revisionData);
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

        return revisions.keySet().size();
    }

    public int getWeight() {

        int weight = 0;
        for (IssueData issue : issues.keySet()) {
            weight += 1; // TODO: replace issue.getWeight();
        }
        return weight;
    }

    public String getWeightFormula() {

        // TODO: make this into something less lame!
        String formula = "";
        String prefix = "";
        for (IssueData issue : issues.keySet()) {
            formula += prefix + issue.getKey(); // TODO: replace + ":" + issue.getWeight();
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
