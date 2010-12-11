package uk.org.sappho.code.heatmap.engine.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.engine.Engine;
import uk.org.sappho.code.heatmap.engine.EngineException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.report.ReportException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.string.mapping.Mapper;

@Singleton
public class Releases implements Engine {

    private final List<String> releaseNames;
    private final Map<String, HeatMaps> releases = new HashMap<String, HeatMaps>();
    private final HeatMapSelector heatMapSelector;
    private final Mapper commitCommentToIssueKeyMapper;
    private final Report report;

    @Inject
    public Releases(Configuration config, HeatMapSelector heatMapSelector,
            @Named("commitCommentToIssueKeyMapper") Mapper commitCommentToIssueKeyMapper, Report report)
            throws ConfigurationException {

        releaseNames = config.getPropertyList("releases");
        this.heatMapSelector = heatMapSelector;
        this.commitCommentToIssueKeyMapper = commitCommentToIssueKeyMapper;
        this.report = report;
    }

    public void run(RawData rawData) throws EngineException {

        try {
            for (String revisionKey : rawData.getRevisionKeys()) {
                RevisionData revisionData = rawData.getRevisionData(revisionKey);
                String commitComment = revisionData.getCommitComment();
                String issueKey = commitCommentToIssueKeyMapper.map(commitComment);
                IssueData issueData = rawData.getIssueData(issueKey);
                List<String> issueReleases = issueData.getReleases();
                for (String issueRelease : issueReleases) {
                    HeatMaps heatMaps = releases.get(issueRelease);
                    if (heatMaps == null) {
                        heatMaps = new HeatMaps();
                        releases.put(issueRelease, heatMaps);
                    }
                    heatMaps.add(revisionData, issueData, heatMapSelector);
                }
            }
            report.writeReport(this);
        } catch (ReportException e) {
            throw new EngineException("HeatMap engine error", e);
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
