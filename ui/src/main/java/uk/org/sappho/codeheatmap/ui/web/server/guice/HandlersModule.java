package uk.org.sappho.codeheatmap.ui.web.server.guice;

import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataDifferencesHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataInfoHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchHeaderNamesComparisonHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchPartiesHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.ImportDataHandler;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparison;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchParties;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportData;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

public class HandlersModule extends ActionHandlerModule {

    @Override
    protected void configureHandlers() {
        bindHandler(FetchParties.class, FetchPartiesHandler.class);
        bindHandler(FetchHeaderNamesComparison.class, FetchHeaderNamesComparisonHandler.class);
        bindHandler(FetchDataInfo.class, FetchDataInfoHandler.class);
        bindHandler(FetchDataDifferences.class, FetchDataDifferencesHandler.class);
        bindHandler(ImportData.class, ImportDataHandler.class);
    }

}
