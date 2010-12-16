package uk.org.sappho.code.heatmap.engine.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.mapping.HeatMapMapping;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;

public class HeatMaps {

    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public void add(RevisionData revisionData, IssueData issueData, HeatMapSelector heatMapSelector) {

        for (String changedFile : revisionData.getChangedFiles()) {
            List<HeatMapMapping> mappings = heatMapSelector.map(changedFile);
            for (HeatMapMapping mapping : mappings) {
                String heatMapName = mapping.getName();
                HeatMap heatMap = heatMaps.get(heatMapName);
                if (heatMap == null) {
                    heatMap = new HeatMap();
                    heatMaps.put(heatMapName, heatMap);
                }
                heatMap.add(mapping.getItem(), revisionData, issueData);
            }
        }
    }

    public List<String> getHeatMapNames() {

        List<String> names = new ArrayList<String>();
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
