package uk.org.sappho.code.heatmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String changedItemName;
    private final Map<IssueData, IssueData> issues = new HashMap<IssueData, IssueData>();
    private final Map<RevisionData, RevisionData> revisions = new HashMap<RevisionData, RevisionData>();

    public HeatMapItem(String changedItemName) {

        this.changedItemName = changedItemName;
    }

    public void add(RevisionData revisionData, IssueData issueData) {

        issues.put(issueData, issueData);
        revisions.put(revisionData, revisionData);
    }

    public String getChangedItemName() {

        return changedItemName;
    }

    public Set<IssueData> getIssues() {

        return issues.keySet();
    }

    public int getIssuesCount() {

        return issues.keySet().size();
    }

    public Set<RevisionData> getRevisions() {

        return revisions.keySet();
    }

    public int getRevisionsCount() {

        return revisions.keySet().size();
    }

    public int getWeight() {

        int weight = 0;
        for (IssueData issue : issues.keySet()) {
            weight++;
            if (issue.getType().equalsIgnoreCase("defect"))
                weight += 2;
        }
        return weight;
    }

    public int compareTo(HeatMapItem other) {

        int comparison = 0;
        comparison = other.getWeight() - getWeight();
        return comparison;
    }
}
