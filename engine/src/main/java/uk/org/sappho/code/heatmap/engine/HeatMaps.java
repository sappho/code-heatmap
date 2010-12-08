package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;

public class HeatMaps {

    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public void add(ChangeSet change, HeatMapSelector heatMapSelector) {

        for (String changedFile : change.getChangedFiles()) {
            List<HeatMapMapping> mappings = heatMapSelector.map(changedFile);
            for (HeatMapMapping mapping : mappings) {
                String heatMapName = mapping.getName();
                HeatMap heatMap = heatMaps.get(heatMapName);
                if (heatMap == null) {
                    heatMap = new HeatMap();
                    heatMaps.put(heatMapName, heatMap);
                }
                heatMap.add(mapping.getItem(), change);
            }
        }
    }

    public List<String> getHeatMapNames() {

        List<String> names = new Vector<String>();
        for (String name : heatMaps.keySet()) {
            if (heatMaps.get(name).getUnsortedHeatMapItems().size() != 0) {
                names.add(name);
            }
        }
        return names;
    }

    public final HeatMap getHeatMap(String heatMapName) {

        return heatMaps.get(heatMapName);
    }
}
