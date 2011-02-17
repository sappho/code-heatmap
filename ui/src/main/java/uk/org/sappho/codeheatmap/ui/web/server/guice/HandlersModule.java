package uk.org.sappho.codeheatmap.ui.web.server.guice;

import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataHandler;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

public class HandlersModule extends HandlerModule {

    @Override
    protected void configureHandlers() {
        bindHandler(FetchData.class, FetchDataHandler.class);
    }

}
