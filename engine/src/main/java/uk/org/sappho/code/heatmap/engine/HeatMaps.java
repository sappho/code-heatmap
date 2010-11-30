package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.Map;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMaps {

    public static final String DIRECTORY = "directories";
    public static final String FILENAME = "filenames";
    public static final String FULLFILENAME = "fullfilenames";
    public static final String CLASSNAME = "classnames";
    public static final String PACKAGENAME = "packagenames";
    public static final String[] HEATMAPS = { DIRECTORY, FILENAME, FULLFILENAME, CLASSNAME, PACKAGENAME };

    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public HeatMaps() {

        for (String heatMapName : HEATMAPS) {
            heatMaps.put(heatMapName, new HeatMap());
        }
    }

    public void update(ChangeSet change) throws IssueManagementException {

        for (ConfigurableItem configurableItem : change.getConfigurableItems()) {
            heatMaps.get(DIRECTORY).update(configurableItem.getDirectory(), change);
            heatMaps.get(FILENAME).update(configurableItem.getFilename(), change);
            heatMaps.get(FULLFILENAME).update(configurableItem.getFullFilename(), change);
            if (configurableItem.isJava()) {
                heatMaps.get(CLASSNAME).update(configurableItem.getClassName(), change);
                heatMaps.get(PACKAGENAME).update(configurableItem.getPackageName(), change);
            }
        }
    }

    public final HeatMap getHeatMap(String heatMapName) {

        return heatMaps.get(heatMapName);
    }
}
