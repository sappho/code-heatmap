package uk.org.sappho.code.heatmap.mapping;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Singleton;

@Singleton
public class SimpleHeatMapSelector implements HeatMapSelector {

    private static final Pattern JAVA_REGEX = Pattern
            .compile("^.+?/((com|org|net|edu|gov|mil|biz|info|uk)/.+)/(.+?)\\.java$");

    public List<HeatMapMapping> map(String changedFile) {

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
        return mappings;
    }
}
