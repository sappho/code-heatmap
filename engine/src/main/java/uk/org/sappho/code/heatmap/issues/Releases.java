package uk.org.sappho.code.heatmap.issues;

import java.util.List;
import java.util.Vector;

public class Releases {

    private final List<String> releases = new Vector<String>();
    private final List<String> warnings = new Vector<String>();

    public void addRelease(String release) {

        releases.add(release);
    }

    public List<String> getReleases() {

        return releases;
    }

    public void addWarning(String warning) {

        warnings.add(warning);
    }

    public List<String> getWarnings() {

        return warnings;
    }
}
