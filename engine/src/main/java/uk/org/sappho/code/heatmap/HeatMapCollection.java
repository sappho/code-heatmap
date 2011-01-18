package uk.org.sappho.code.heatmap;

import java.util.List;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public interface HeatMapCollection {

    public void add(RevisionData revisionData, IssueData issueData);

    public List<String> getHeatMapNames();

    public HeatMap getHeatMap(String name);
}
