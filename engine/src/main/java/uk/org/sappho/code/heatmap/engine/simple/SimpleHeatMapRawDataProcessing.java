package uk.org.sappho.code.heatmap.engine.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.engine.RawDataProcessing;
import uk.org.sappho.code.change.management.engine.RawDataProcessingException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.report.ReportException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@Singleton
public class SimpleHeatMapRawDataProcessing implements RawDataProcessing {

    private List<String> releaseNames;
    private final Map<String, HeatMaps> heatMapsPerRelease = new HashMap<String, HeatMaps>();
    private final Report report;
    private final HeatMapSelector heatMapSelector;

    @Inject
    public SimpleHeatMapRawDataProcessing(Configuration config, Report report) throws ConfigurationException {

        heatMapSelector = (HeatMapSelector) config.getGroovyScriptObject("mapper.heatmap.selector");
        this.report = report;
    }

    public void run(RawData rawData) throws RawDataProcessingException {

        try {
            releaseNames = new ArrayList<String>();
            for (String revisionKey : rawData.getRevisionKeys()) {
                RevisionData revisionData = rawData.getRevisionData(revisionKey);
                String issueKey = revisionData.getIssueKey();
                IssueData issueData = rawData.getIssueData(issueKey);
                if (issueData != null) {
                    List<String> issueReleases = issueData.getReleases();
                    for (String issueRelease : issueReleases) {
                        if (!releaseNames.contains(issueRelease)) {
                            releaseNames.add(issueRelease);
                        }
                        HeatMaps heatMaps = heatMapsPerRelease.get(issueRelease);
                        if (heatMaps == null) {
                            heatMaps = new HeatMaps();
                            heatMapsPerRelease.put(issueRelease, heatMaps);
                        }
                        heatMaps.add(revisionData, issueData, heatMapSelector);
                    }
                }
            }
            report.writeReport(this, rawData.getWarnings());
        } catch (ReportException e) {
            throw new RawDataProcessingException("HeatMap engine error", e);
        }
    }

    public final List<String> getUsedReleaseNames() {

        return releaseNames;
    }

    public final HeatMaps getHeatMaps(String releaseName) {

        return heatMapsPerRelease.get(releaseName);
    }
}
