package uk.org.sappho.code.heatmap.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.code.change.management.data.ChangedFile;
import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.HeatMap;
import uk.org.sappho.code.heatmap.HeatMapCollection;
import uk.org.sappho.code.heatmap.selection.HeatMapMapping;
import uk.org.sappho.code.heatmap.selection.HeatMapSelector;

public class BasicHeatMapCollection implements HeatMapCollection {

    private final HeatMapSelector heatMapSelector;
    private final Provider<HeatMap> heatMapProvider;
    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    @Inject
    public BasicHeatMapCollection(HeatMapSelector heatMapSelector, Provider<HeatMap> heatMapProvider) {

        this.heatMapSelector = heatMapSelector;
        this.heatMapProvider = heatMapProvider;
    }

    public void add(RevisionData revisionData, IssueData issueData) {

        for (ChangedFile changedFile : revisionData.getChangedFiles()) {
            List<HeatMapMapping> mappings = heatMapSelector.map(changedFile.getFilename());
            for (HeatMapMapping mapping : mappings) {
                String heatMapName = mapping.getName();
                HeatMap heatMap = heatMaps.get(heatMapName);
                if (heatMap == null) {
                    heatMap = heatMapProvider.get();
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

    public final HeatMap getHeatMap(String name) {

        return heatMaps.get(name);
    }
}
