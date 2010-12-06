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
            File file = new File(changedFile);
            update("directories", file.getParent(), change);
            update("filenames", file.getName(), change);
            update("fullfilenames", changedFile, change);
            Matcher javaMatcher = JAVA_REGEX.matcher(changedFile);
            if (javaMatcher.matches()) {
                update("classnames", javaMatcher.group(3), change);
                update("packagenames", javaMatcher.group(1).replace('/', '.'), change);
            }
        }
    }

    private void update(String heatMapName, String changedItemName, ChangeSet change)
            throws IssueManagementException {

        HeatMap heatMap = heatMaps.get(heatMapName);
        if (heatMap == null) {
            heatMap = new HeatMap();
            heatMaps.put(heatMapName, heatMap);
        }
        heatMap.update(changedItemName, change);
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
