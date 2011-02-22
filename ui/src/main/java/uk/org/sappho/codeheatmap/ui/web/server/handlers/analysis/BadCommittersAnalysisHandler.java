package uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.filter;
import static org.hamcrest.Matchers.allOf;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Augment.intoAugmentedRevisions;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.RevisionMatchers.isntAMerge;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.RevisionMatchers.issueKeyPrefix;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Sorters.byDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.AugmentedRevisionData;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.BaseDataAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.BadCommittersAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.BadCommittersAnalysisResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class BadCommittersAnalysisHandler extends BaseDataAnalysis<BadCommittersAnalysis, BadCommittersAnalysisResult> {

    private static final Logger LOG = Logger.getLogger(BadCommittersAnalysisHandler.class);

    @Inject
    public BadCommittersAnalysisHandler(ReaderRawDataPersistence rawDataPersistence) {
        super(rawDataPersistence);
    }

    @Override
    public BadCommittersAnalysisResult execute(BadCommittersAnalysis arg0, ExecutionContext arg1)
            throws ActionException {

        RawData rawData = getRawData();

        List<RevisionData> revisions = new ArrayList<RevisionData>(rawData.getRevisionDataMap().values());
        List<AugmentedRevisionData> revisionsWithReleases = convert(revisions, intoAugmentedRevisions(rawData));
        List<AugmentedRevisionData> excludeSCMAndMerges = filter(
                allOf(issueKeyPrefix("SCM"),
                        isntAMerge()),
                        revisionsWithReleases);
        Collections.sort(excludeSCMAndMerges, byDate());

        BadCommitters badCommitters = new BadCommitters();
        for (RevisionData revision : excludeSCMAndMerges) {
            badCommitters.addChangeSet(revision.getChangedFiles(), revision.getDate(), revision.getCommitter(),
                    revision.getIssueKey(), rawData.getIssueData(revision.getIssueKey()).getType());
        }
        LOG.info("Found " + badCommitters.getCommitters().size() + " committers in bad committers analysis");
        return new BadCommittersAnalysisResult(badCommitters.getList());
    }

    @Override
    public void undo(BadCommittersAnalysis arg0, BadCommittersAnalysisResult arg1, ExecutionContext arg2)
            throws ActionException {
    }

    @Override
    public Class<BadCommittersAnalysis> getActionType() {
        return BadCommittersAnalysis.class;
    }

}
