package uk.org.sappho.code.heatmap.engine;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;
import uk.org.sappho.code.heatmap.data.RawData;
import uk.org.sappho.code.heatmap.issues.IssueManagementException;
import uk.org.sappho.code.heatmap.issues.IssueWrapper;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;

public class Releases {

    private final List<String> releaseNames;
    private final String storeFilename;
    private final Map<String, HeatMaps> releases = new HashMap<String, HeatMaps>();
    private RawData releasesRawData = new RawData();
    private final XStream xstream = new XStream();
    private final HeatMapSelector heatMapSelector;
    private static final Logger LOG = Logger.getLogger(Releases.class);

    @Inject
    public Releases(Configuration config, HeatMapSelector heatMapSelector) throws ConfigurationException {

        releaseNames = config.getPropertyList("releases");
        storeFilename = config.getProperty("releases.store.filename", "heatmap-data.xml");
        for (Class clazz : new Class[] { RawData.class, IssueWrapper.class, ChangeSet.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
        this.heatMapSelector = heatMapSelector;
    }

    public void add(ChangeSet change) {

        add(change, true);
    }

    private void add(ChangeSet change, boolean updateRawData) {

        if (updateRawData) {
            releasesRawData.add(change);
        }
        List<String> issueReleases = change.getIssue().getReleases();
        for (String issueRelease : issueReleases) {
            HeatMaps heatMaps = releases.get(issueRelease);
            if (heatMaps == null) {
                heatMaps = new HeatMaps();
                releases.put(issueRelease, heatMaps);
            }
            heatMaps.add(change, heatMapSelector);
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

    @SuppressWarnings("unchecked")
    public void load() throws IOException, IssueManagementException {

        LOG.info("Loading data from " + storeFilename);
        Reader reader = new FileReader(storeFilename);
        releasesRawData = (RawData) xstream.fromXML(reader);
        reader.close();
        for (ChangeSet change : releasesRawData.getChangeSets()) {
            add(change, false);
        }
    }

    public void save() throws IOException {

        LOG.info("Saving data to " + storeFilename);
        Writer writer = new FileWriter(storeFilename);
        xstream.toXML(releasesRawData, writer);
        writer.close();
    }
}
