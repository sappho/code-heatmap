package uk.org.sappho.code.heatmap.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMap {

    private final Map<String, HeatMapItem> heatMap = new HashMap<String, HeatMapItem>();

    public void update(String name, ChangeSet change) throws IssueManagementException {

        HeatMapItem item = heatMap.get(name);
        if (item == null) {
            item = new HeatMapItem(name);
            heatMap.put(name, item);
        }
        item.update(change);
    }

    public List<HeatMapItem> getSortedList() {

        List<HeatMapItem> list = new Vector<HeatMapItem>(heatMap.values());
        Collections.sort(list);
        return list;
    }
}
