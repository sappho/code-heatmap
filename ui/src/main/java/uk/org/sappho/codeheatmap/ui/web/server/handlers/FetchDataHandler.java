package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ReleaseChangesDefects;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData.FetchDataType;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;
import ch.lambdaj.function.convert.Converter;
import ch.lambdaj.group.Group;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class FetchDataHandler extends BaseDataAnalysis<FetchData, FetchDataResult> {

    private static final Logger LOG = Logger.getLogger(FetchDataHandler.class);

    @Inject
    public FetchDataHandler(ReaderRawDataPersistence rawDataPersistence) {
        super(rawDataPersistence);
    }

    @Override
    public Class<FetchData> getActionType() {
        return FetchData.class;
    }

    @Override
    public FetchDataResult execute(FetchData action, ExecutionContext context)
            throws ActionException {

        RawData rawData = getRawData();
        List<ReleaseChangesDefects> data = null;
        if (action.getFetchDataType() == FetchDataType.ISSUES) {
            data = massageIssueData(rawData);
        } else if (action.getFetchDataType() == FetchDataType.REVISIONS) {
            data = massageRevisionData(rawData);
        }
        return new FetchDataResult(data);

    }

    private List<ReleaseChangesDefects> massageRevisionData(RawData rawData) {
        Collection<RevisionData> revisions = rawData.getRevisionDataMap().values();
        List<AugmentedRevisionData> revisionsWithReleases = convert(revisions, intoAugmentedRevisions(rawData));
        Group<AugmentedRevisionData> releasesGroup = group(revisionsWithReleases,
                by(on(AugmentedRevisionData.class).getMainRelease()),
                by(on(AugmentedRevisionData.class).getType()));
        ArrayList<String> releaseNames = new ArrayList<String>(releasesGroup.keySet());
        Collections.sort(releaseNames, new SortedAsIfNumbers());
        LOG.info("Found " + releaseNames.size() + " releases");

        List<ReleaseChangesDefects> data = new ArrayList<ReleaseChangesDefects>();
        for (String release : releaseNames) {
            Group<AugmentedRevisionData> releaseGroup = releasesGroup.findGroup(release);
            List<AugmentedRevisionData> changes = releaseGroup.find("change");
            List<AugmentedRevisionData> defects = releaseGroup.find("defect");
            if (release != null && !release.isEmpty()) {
                data.add(new ReleaseChangesDefects(release, changes.size(), defects.size()));
            }
        }
        LOG.info("Finished massaging");
        return data;

    }

    private Converter<RevisionData, AugmentedRevisionData> intoAugmentedRevisions(final RawData rawData) {
        return new Converter<RevisionData, AugmentedRevisionData>() {
            @Override
            public AugmentedRevisionData convert(RevisionData from) {
                AugmentedRevisionData aug = new AugmentedRevisionData(
                        from.getIssueKey(),
                        from.getDate(),
                        from.getCommitComment(),
                        from.getCommitter(),
                        from.getChangedFiles(),
                        from.getIssueKey());
                IssueData issueData = rawData.getIssueData(aug.getIssueKey());
                if (issueData != null) {
                    aug.setMainRelease(issueData.getMainRelease());
                    aug.setType(issueData.getType());
                }
                return aug;
            }
        };
    }

    private List<ReleaseChangesDefects> massageIssueData(RawData rawData) {

        Collection<IssueData> issues = rawData.getIssueDataMap().values();
        Group<IssueData> releasesGroup = group(issues,
                by(on(IssueData.class).getMainRelease()),
                by(on(IssueData.class).getType()));
        ArrayList<String> releaseNames = new ArrayList<String>(releasesGroup.keySet());
        Collections.sort(releaseNames, new SortedAsIfNumbers());
        LOG.info("Found " + releaseNames.size() + " releases");

        List<ReleaseChangesDefects> data = new ArrayList<ReleaseChangesDefects>();
        for (String release : releaseNames) {
            Group<IssueData> releaseGroup = releasesGroup.findGroup(release);
            List<IssueData> changes = releaseGroup.find("change");
            List<IssueData> defects = releaseGroup.find("defect");
            if (release != null && !release.isEmpty()) {
                data.add(new ReleaseChangesDefects(release, changes.size(), defects.size()));
            }
        }
        LOG.info("Finished massaging");
        return data;
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

    @Override
    public void undo(FetchData arg0, FetchDataResult arg1, ExecutionContext arg2) throws ActionException {

    }
}
