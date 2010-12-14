package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.HeaderMatcher;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparison;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparisonResult;

public class FetchHeaderNamesComparisonHandler implements
        ActionHandler<FetchHeaderNamesComparison, FetchHeaderNamesComparisonResult> {

    private final HeaderMatcher headerMatcher;

    @Inject
    public FetchHeaderNamesComparisonHandler(HeaderMatcher headerMatcher) {
        this.headerMatcher = headerMatcher;
    }

    @Override
    public Class<FetchHeaderNamesComparison> getActionType() {
        return FetchHeaderNamesComparison.class;
    }

    @Override
    public FetchHeaderNamesComparisonResult execute(FetchHeaderNamesComparison action, ExecutionContext context)
            throws ActionException {

        headerMatcher.match();
        return new FetchHeaderNamesComparisonResult(headerMatcher.getHeaderMatches());
    }

    @Override
    public void rollback(FetchHeaderNamesComparison action, FetchHeaderNamesComparisonResult result,
            ExecutionContext context)
            throws ActionException {

    }

}
