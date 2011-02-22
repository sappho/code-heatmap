package uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis;

import static ch.lambdaj.Lambda.join;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Normalisers.normaliseFilename;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import uk.org.sappho.code.change.management.data.ChangedFile;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.BaseDataAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.ChurnAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.ChurnAnalysisResult;

public class ChurnAnalysisHandler extends BaseDataAnalysis<ChurnAnalysis, ChurnAnalysisResult> {

    private static final Logger LOG = Logger.getLogger(ChurnAnalysisHandler.class);

    @Inject
    public ChurnAnalysisHandler(ReaderRawDataPersistence rawDataPersistence) {
        super(rawDataPersistence);
    }

    @Override
    public ChurnAnalysisResult execute(ChurnAnalysis arg0, ExecutionContext arg1) throws ActionException {

        Map<String, Integer> filenameChangeCount = new HashMap<String, Integer>();
        Map<String, Integer> worstOffenders = new HashMap<String, Integer>();

        RawData rawData = getRawData();
        Collection<RevisionData> revisions = rawData.getRevisionDataMap().values();

        for (RevisionData revision : revisions) {
            for (ChangedFile filename : revision.getChangedFiles()) {
                if (!filename.getFilename().endsWith(".java") || revision.isMerge()) {
                    continue;
                }
                String normalisedFilename = normaliseFilename(filename.getFilename());
                Integer count = filenameChangeCount.get(normalisedFilename);
                if (count == null) {
                    filenameChangeCount.put(normalisedFilename, 1);
                } else {
                    filenameChangeCount.put(normalisedFilename, count + 1);
                    if (count > 400) {
                        worstOffenders.put(normalisedFilename, count + 1);
                    }
                }
            }
        }

        ArrayList<Integer> numbers = new ArrayList<Integer>(filenameChangeCount.values());
        Collections.sort(numbers);
        LOG.info("Hi-lo: " + numbers.get(0) + "-" + numbers.get(numbers.size() - 1));

        LOG.info(numbers.size() + " files found in churn analysis");

        return new ChurnAnalysisResult(numbers, join(worstOffenders.keySet()));
    }

    @Override
    public void undo(ChurnAnalysis arg0, ChurnAnalysisResult arg1, ExecutionContext arg2) throws ActionException {
    }

    @Override
    public Class<ChurnAnalysis> getActionType() {
        return ChurnAnalysis.class;
    }

}
