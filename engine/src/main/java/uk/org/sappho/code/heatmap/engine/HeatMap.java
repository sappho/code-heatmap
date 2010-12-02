package uk.org.sappho.code.heatmap.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMap {

    private final Map<String, HeatMapItem> heatMapItems = new HashMap<String, HeatMapItem>();

    public void update(String configurableItemName, ChangeSet changeSet) throws IssueManagementException {

        HeatMapItem item = heatMapItems.get(configurableItemName);
        if (item == null) {
            item = new HeatMapItem(configurableItemName);
            heatMapItems.put(configurableItemName, item);
        }
        item.update(changeSet);
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
