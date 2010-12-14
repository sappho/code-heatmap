package uk.org.sappho.codeheatmap.ui.web.server.guice;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataHandler;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

public class HandlersModule extends ActionHandlerModule {

    @Override
    protected void configureHandlers() {
        bindHandler(FetchData.class, FetchDataHandler.class);
    }

}
