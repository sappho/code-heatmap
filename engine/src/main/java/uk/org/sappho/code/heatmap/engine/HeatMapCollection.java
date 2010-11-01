package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.Map;

public class HeatMapCollection {

    public static final String DIRECTORY = "Directories";
    public static final String FILENAME = "File names";
    public static final String FULLFILENAME = "Full file names";
    public static final String CLASSNAME = "Class names";
    public static final String PACKAGENAME = "Package names";
    public static final String[] HEATMAPS = { DIRECTORY, FILENAME, FULLFILENAME, CLASSNAME, PACKAGENAME };

    private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

    public HeatMapCollection() {

        for (String name : HEATMAPS) {
            heatMaps.put(name, new HeatMap(name));
        }
    }

    public void update(Change change) {

        for (Filename changedFile : change.getChangedFiles()) {
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

    @Override
    public String toString() {

        String str = "";
        for (String name : HEATMAPS) {
            str += "\n" + getHeatMap(name).toString();
        }
        return str;
    }
}
