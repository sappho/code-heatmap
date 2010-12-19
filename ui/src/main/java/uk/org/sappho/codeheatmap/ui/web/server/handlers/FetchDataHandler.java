package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.persistence.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;
import ch.lambdaj.group.Group;

import com.google.inject.Inject;

public class FetchDataHandler implements ActionHandler<FetchData, FetchDataResult> {

    private static final Logger LOG = Logger.getLogger(FetchDataHandler.class);
    private final ReaderRawDataPersistence rawDataPersistence;

    @Inject
    public FetchDataHandler(ReaderRawDataPersistence rawDataPersistence) {
        this.rawDataPersistence = rawDataPersistence;
    }

    @Override
    public Class<FetchData> getActionType() {
        return FetchData.class;
    }

    @Override
    public FetchDataResult execute(FetchData action, ExecutionContext context)
            throws DispatchException {

        File dataFile = new File("WEB-INF/data/raw-data-all-frame-2010-12-14.zip");
        try {
            if (dataFile.exists()) {
                Reader reader = getDataFileReader(dataFile);
                RawData rawData = rawDataPersistence.load(reader);
                List<DataItem> data = massageData(rawData);
                return new FetchDataResult(data);
            } else {
                LOG.error("Couldn't find data file: " + dataFile.getCanonicalPath());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<DataItem> massageData(RawData rawData) {

        Collection<IssueData> issues = rawData.getIssueDataMap().values();
        Group<IssueData> releasesGroup = group(issues,
                by(on(IssueData.class).getMainRelease()),
                by(on(IssueData.class).getType()));
        ArrayList<String> releaseNames = new ArrayList<String>(releasesGroup.keySet());
        Collections.sort(releaseNames, new SortedAsIfNumbers());
        LOG.info("Found " + releaseNames.size() + " releases");

        List<DataItem> data = new ArrayList<DataItem>();
        for (String release : releaseNames) {
            Group<IssueData> releaseGroup = releasesGroup.findGroup(release);
            List<IssueData> changes = releaseGroup.find("change");
            List<IssueData> defects = releaseGroup.find("defect");
            if (release != null && !release.isEmpty()) {
                data.add(new DataItem(release, changes.size(), defects.size()));
            }
        }
        return data;
    }

    private Reader getDataFileReader(File dataFile) throws IOException, ZipException {
        LOG.info("Fetching data from: " + dataFile.getCanonicalFile());
        ZipFile zipFile = new ZipFile(dataFile);
        LOG.info("Found " + zipFile.size() + " entries in zip file");
        LOG.info("First file: " + zipFile.entries().nextElement().getName());
        String entryName = zipFile.entries().nextElement().getName();
        ZipEntry entry = zipFile.getEntry(entryName);
        InputStream inputStream = zipFile.getInputStream(entry);
        Reader reader = new InputStreamReader(inputStream);
        return reader;
    }

    @Override
    public void rollback(FetchData action, FetchDataResult result, ExecutionContext context)
            throws DispatchException {
    }

    private final class SortedAsIfNumbers implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1 == null || o1.isEmpty())
                return -1;
            if (o2 == null || o2.isEmpty())
                return 1;
            try {
                BigDecimal o1AsBD = new BigDecimal(o1);
                BigDecimal o2asBD = new BigDecimal(o2);
                return o1AsBD.compareTo(o2asBD);
            } catch (NumberFormatException nfe) {
                LOG.error("NFE on: " + o1 + ", " + o2);
                return 0;
            }
        }
    }
}
