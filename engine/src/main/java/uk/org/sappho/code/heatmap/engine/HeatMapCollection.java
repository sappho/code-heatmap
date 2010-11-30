package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.Map;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class HeatMapCollection {

    public static final String DIRECTORY = "directories";
    public static final String FILENAME = "filenames";
    public static final String FULLFILENAME = "fullfilenames";
    public static final String CLASSNAME = "classnames";
    public static final String PACKAGENAME = "packagenames";
    public static final String[] HEATMAPS = { DIRECTORY, FILENAME, FULLFILENAME, CLASSNAME, PACKAGENAME };

    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public HeatMapCollection() {

        for (String name : HEATMAPS) {
            heatMaps.put(name, new HeatMap());
        }
    }

    public void update(ChangeSet change) throws IssueManagementException {

        for (ConfigurableItem changedFile : change.getConfigurableItems()) {
            heatMaps.get(DIRECTORY).update(changedFile.getDirectory(), change);
            heatMaps.get(FILENAME).update(changedFile.getFilename(), change);
            heatMaps.get(FULLFILENAME).update(changedFile.getFullFilename(), change);
            if (changedFile.isJava()) {
                heatMaps.get(CLASSNAME).update(changedFile.getClassName(), change);
                heatMaps.get(PACKAGENAME).update(changedFile.getPackageName(), change);
            }
        }
    }

    public final HeatMap getHeatMap(String name) {

        return heatMaps.get(name);
    }
}
