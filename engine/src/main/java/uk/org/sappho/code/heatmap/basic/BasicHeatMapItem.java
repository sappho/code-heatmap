package uk.org.sappho.code.heatmap.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.HeatMapItem;
import uk.org.sappho.code.heatmap.weight.WeightCalculator;

public class BasicHeatMapItem implements HeatMapItem {

    private String changedItemName;
    private final Map<IssueData, IssueData> issues = new HashMap<IssueData, IssueData>();
    private final Map<RevisionData, RevisionData> revisions = new HashMap<RevisionData, RevisionData>();
    private final WeightCalculator weightCalculator;

    @Inject
    public BasicHeatMapItem(Provider<WeightCalculator> weightCalculatorProvider) {

        weightCalculator = weightCalculatorProvider.get();
    }

    public void setChangedItemName(String changedItemName) {

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

        return weightCalculator.getWeight(getRevisions(), getIssues());
    }

    public int compareTo(HeatMapItem other) {

        int comparison = 0;
        comparison = other.getWeight() - getWeight();
        return comparison;
    }
}
