package uk.org.sappho.code.heatmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class HeatMap {

    private final Map<String, HeatMapItem> heatMapItems = new HashMap<String, HeatMapItem>();

    public void add(String changedItemName, RevisionData revisionData, IssueData issueData) {

        HeatMapItem item = heatMapItems.get(changedItemName);
        if (item == null) {
            item = new HeatMapItem(changedItemName);
            heatMapItems.put(changedItemName, item);
        }
        item.add(revisionData, issueData);
    }

    public List<HeatMapItem> getUnsortedHeatMapItems() {

        return new ArrayList<HeatMapItem>(heatMapItems.values());
    }

    public List<HeatMapItem> getSortedHeatMapItems() {

        List<HeatMapItem> list = getUnsortedHeatMapItems();
        Collections.sort(list);
        return list;
    }
}
