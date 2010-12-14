package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.AnalyseDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferencesResult;

public class FetchDataDifferencesHandler implements ActionHandler<FetchDataDifferences, FetchDataDifferencesResult> {

    private final AnalyseDifferences analyseDifferences;

    @Inject
    public FetchDataDifferencesHandler(AnalyseDifferences analyseDifferences) {
        this.analyseDifferences = analyseDifferences;
    }

    @Override
    public Class<FetchDataDifferences> getActionType() {
        return FetchDataDifferences.class;
    }

    @Override
    public FetchDataDifferencesResult execute(FetchDataDifferences action, ExecutionContext context)
            throws DispatchException {

        analyseDifferences.analyse();
        return new FetchDataDifferencesResult(analyseDifferences.getDifferences());
    }

    @Override
    public void rollback(FetchDataDifferences action, FetchDataDifferencesResult result, ExecutionContext context)
            throws DispatchException {

    }

}
