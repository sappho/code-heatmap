package uk.org.sappho.code.heatmap.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.HeatMap;
import uk.org.sappho.code.heatmap.HeatMapItem;

public class BasicHeatMap implements HeatMap {

    private final Provider<HeatMapItem> heatMapItemProvider;
    private final Map<String, HeatMapItem> heatMapItems = new HashMap<String, HeatMapItem>();

    @Inject
    public BasicHeatMap(Provider<HeatMapItem> heatMapItemProvider) {

        this.heatMapItemProvider = heatMapItemProvider;
    }

    public void add(String changedItemName, RevisionData revisionData, IssueData issueData) {

        HeatMapItem item = heatMapItems.get(changedItemName);
        if (item == null) {
            item = heatMapItemProvider.get();
            item.setChangedItemName(changedItemName);
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
