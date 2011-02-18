package uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Augment.intoAugmentedRevisions;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Normalisers.extractPackage;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.RevisionMatchers.isProductionJava;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Sorters.asIfNumbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.AugmentedRevisionData;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.BaseDataAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.TopChurnPerReleaseAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.TopChurnPerReleaseAnalysisResult;
import ch.lambdaj.group.Group;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class TopChurnPerReleaseAnalysisHandler extends
        BaseDataAnalysis<TopChurnPerReleaseAnalysis, TopChurnPerReleaseAnalysisResult> {

    private static final Logger LOG = Logger.getLogger(TopChurnPerReleaseAnalysisHandler.class);

    @Inject
    public TopChurnPerReleaseAnalysisHandler(ReaderRawDataPersistence rawDataPersistence) {
        super(rawDataPersistence);
    }

    @Override
    public TopChurnPerReleaseAnalysisResult execute(TopChurnPerReleaseAnalysis arg0, ExecutionContext arg1)
            throws ActionException {

        RawData rawData = getRawData();

        List<RevisionData> revisions = new ArrayList<RevisionData>(rawData.getRevisionDataMap().values());
        List<AugmentedRevisionData> revisionsWithReleases = convert(revisions, intoAugmentedRevisions(rawData));
        Group<AugmentedRevisionData> releasesGroup = group(revisionsWithReleases,
                by(on(AugmentedRevisionData.class).getMainRelease()),
                by(on(AugmentedRevisionData.class).getType()));
        ArrayList<String> releaseNames = new ArrayList<String>(releasesGroup.keySet());
        Collections.sort(releaseNames, asIfNumbers());
        LOG.info("Found " + releaseNames.size() + " releases");

        for (String release : releaseNames) {
            Group<AugmentedRevisionData> releaseGroup = releasesGroup.findGroup(release);
            List<AugmentedRevisionData> changes = releaseGroup.find("change");

            List<AugmentedRevisionData> defects = releaseGroup.find("defect");
            FileCounter fileCounter = new FileCounter();
            for (AugmentedRevisionData defect : defects) {
                for (String file : defect.getChangedFiles()) {
                    if (isProductionJava().matches(file)) {
                        fileCounter.addOne(extractPackage(file));
                    }
                }
            }
            LOG.info(" == " + release + " == ");
            for (FileCounter.Entry entry : fileCounter.topItems(5)) {
                LOG.info(entry.getFile() + ": " + entry.getCount());
            }

        }
        return new TopChurnPerReleaseAnalysisResult();
    }

    @Override
    public void undo(TopChurnPerReleaseAnalysis arg0, TopChurnPerReleaseAnalysisResult arg1, ExecutionContext arg2)
            throws ActionException {
    }

    @Override
    public Class<TopChurnPerReleaseAnalysis> getActionType() {
        return TopChurnPerReleaseAnalysis.class;
    }

}
