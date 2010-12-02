package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class Releases {

    private final List<String> releaseNames;
    private final Map<String, HeatMaps> releases = new HashMap<String, HeatMaps>();

    @Inject
    public Releases(Configuration config) throws ConfigurationException {

        releaseNames = config.getPropertyList("releases");
    }

    public void update(ChangeSet change) throws IssueManagementException {

        List<String> issueReleases = change.getIssue().getReleases();
        for (String issueRelease : issueReleases) {
            HeatMaps heatMaps = releases.get(issueRelease);
            if (heatMaps == null) {
                heatMaps = new HeatMaps();
                releases.put(issueRelease, heatMaps);
            }
            heatMaps.update(change);
        }
    }

    public final List<String> getReleaseNames() {

        return releaseNames;
    }

    public final HeatMaps getHeatMaps(String release) {

        return releases.get(release);
    }
}
