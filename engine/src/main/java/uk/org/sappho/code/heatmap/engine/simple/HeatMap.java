package uk.org.sappho.code.heatmap.engine.simple;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.change.management.data.ChangeSet;

public class HeatMap {

    private final Map<String, HeatMapItem> heatMapItems = new HashMap<String, HeatMapItem>();

    public void add(String configurableItemName, ChangeSet changeSet) {

        HeatMapItem item = heatMapItems.get(configurableItemName);
        if (item == null) {
            item = new HeatMapItem(configurableItemName);
            heatMapItems.put(configurableItemName, item);
        }
        item.add(changeSet);
    }

    public List<HeatMapItem> getUnsortedHeatMapItems() {

        return new Vector<HeatMapItem>(heatMapItems.values());
    }

    public List<HeatMapItem> getSortedHeatMapItems() {

        List<HeatMapItem> list = new Vector<HeatMapItem>(heatMapItems.values());
        Collections.sort(list);
        return list;
    }
}