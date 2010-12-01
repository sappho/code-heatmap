package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.sappho.code.heatmap.issues.IssueManagementException;

public class Releases {

    private final Map<String, HeatMaps> releases = new HashMap<String, HeatMaps>();

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

    public final Set<String> getReleaseNames() {

        return releases.keySet();
    }

    public final HeatMaps getHeatMaps(String release) {

        return releases.get(release);
    }
}
