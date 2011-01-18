package uk.org.sappho.code.heatmap.weight;

import java.util.Collection;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public interface WeightCalculator {

    public int getWeight(Collection<RevisionData> revisions, Collection<IssueData> issues);
}
