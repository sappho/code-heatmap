package uk.org.sappho.code.heatmap;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public interface HeatMap {

    public void add(String changedItemName, RevisionData revisionData, IssueData issueData);

    public List<HeatMapItem> getUnsortedHeatMapItems();

    public List<HeatMapItem> getSortedHeatMapItems();
}
