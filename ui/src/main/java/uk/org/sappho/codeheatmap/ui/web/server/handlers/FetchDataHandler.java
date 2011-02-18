package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Augment.intoAugmentedRevisions;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Sorters.asIfNumbers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData.FetchDataType;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ReleaseChangesDefects;
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
        Collections.sort(releaseNames, asIfNumbers());
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

    private List<ReleaseChangesDefects> massageIssueData(RawData rawData) {

        Collection<IssueData> issues = rawData.getIssueDataMap().values();
        Group<IssueData> releasesGroup = group(issues,
                by(on(IssueData.class).getMainRelease()),
                by(on(IssueData.class).getType()));
        ArrayList<String> releaseNames = new ArrayList<String>(releasesGroup.keySet());
        Collections.sort(releaseNames, asIfNumbers());
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

    @Override
    public void undo(FetchData arg0, FetchDataResult arg1, ExecutionContext arg2) throws ActionException {

    }
}
