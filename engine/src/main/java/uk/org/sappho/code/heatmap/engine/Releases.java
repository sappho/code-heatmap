package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;

public class Releases {

    private final List<String> releaseNames;
    private final Map<String, HeatMaps> releases = new HashMap<String, HeatMaps>();
    private final HeatMapSelector heatMapSelector;

    @Inject
    public Releases(Configuration config, HeatMapSelector heatMapSelector) throws ConfigurationException {

        releaseNames = config.getPropertyList("releases");
        this.heatMapSelector = heatMapSelector;
    }

    public void update(ChangeSet change) throws IssueManagementException {

        List<String> issueReleases = change.getIssue().getReleases();
        for (String issueRelease : issueReleases) {
            HeatMaps heatMaps = releases.get(issueRelease);
            if (heatMaps == null) {
                heatMaps = new HeatMaps(heatMapSelector);
                releases.put(issueRelease, heatMaps);
            }
            heatMaps.update(change);
        }
    }

    public final List<String> getUsedReleaseNames() {

        List<String> usedReleaseNames = new Vector<String>();
        for (String releaseName : releaseNames) {
            if (releases.get(releaseName) != null) {
                usedReleaseNames.add(releaseName);
            }
        }
        return usedReleaseNames;
    }

    public final HeatMaps getHeatMaps(String releaseName) {

        return releases.get(releaseName);
    }
}
