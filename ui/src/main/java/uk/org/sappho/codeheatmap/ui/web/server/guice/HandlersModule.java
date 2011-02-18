package uk.org.sappho.codeheatmap.ui.web.server.guice;

import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis.BadCommittersAnalysisHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis.ChurnAnalysisHandler;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis.TopChurnPerReleaseAnalysisHandler;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.BadCommittersAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.ChurnAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.TopChurnPerReleaseAnalysis;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

public class HandlersModule extends HandlerModule {

    @Override
    protected void configureHandlers() {
        bindHandler(FetchData.class, FetchDataHandler.class);
        bindHandler(ChurnAnalysis.class, ChurnAnalysisHandler.class);
        bindHandler(BadCommittersAnalysis.class, BadCommittersAnalysisHandler.class);
        bindHandler(TopChurnPerReleaseAnalysis.class, TopChurnPerReleaseAnalysisHandler.class);
    }

}
