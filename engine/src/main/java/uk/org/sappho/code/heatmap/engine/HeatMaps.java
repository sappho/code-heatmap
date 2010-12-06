package uk.org.sappho.code.heatmap.engine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMaps {

    private static final Pattern JAVA_REGEX = Pattern
            .compile("^.+?/((com|org|net|edu|gov|mil|biz|info)/.+)/(.+?)\\.java$");
    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public void update(ChangeSet change) throws IssueManagementException {

        for (String changedFile : change.getChangedFiles()) {
            List<HeatMapMapping> mappings = new Vector<HeatMapMapping>();
            File file = new File(changedFile);
            mappings.add(new HeatMapMapping("directories", file.getParent()));
            mappings.add(new HeatMapMapping("filenames", file.getName()));
            mappings.add(new HeatMapMapping("fullfilenames", changedFile));
            Matcher javaMatcher = JAVA_REGEX.matcher(changedFile);
            if (javaMatcher.matches()) {
                mappings.add(new HeatMapMapping("classnames", javaMatcher.group(3)));
                mappings.add(new HeatMapMapping("packagenames", javaMatcher.group(1).replace('/', '.')));
            }
            for (HeatMapMapping mapping : mappings) {
                String heatMapName = mapping.getName();
                HeatMap heatMap = heatMaps.get(heatMapName);
                if (heatMap == null) {
                    heatMap = new HeatMap();
                    heatMaps.put(heatMapName, heatMap);
                }
                heatMap.update(mapping.getItem(), change);
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
