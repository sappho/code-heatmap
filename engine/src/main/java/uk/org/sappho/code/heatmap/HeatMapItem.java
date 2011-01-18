package uk.org.sappho.code.heatmap;

import java.util.Set;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public interface HeatMapItem extends Comparable<HeatMapItem> {

    public void setChangedItemName(String changedItemName);

    public void add(RevisionData revisionData, IssueData issueData);

    public String getChangedItemName();

    public Set<IssueData> getIssues();

    public int getIssuesCount();

    public Set<RevisionData> getRevisions();

    public int getRevisionsCount();

    public int getWeight();
}
