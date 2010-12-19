package uk.org.sappho.codeheatmap.ui.web.server.guice;

import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;

import com.google.inject.servlet.ServletModule;

public class DispatchServletModule extends ServletModule {

    @Override
    public void configureServlets() {
        serve("/codeheatmap/dispatch").with(GuiceStandardDispatchServlet.class);
    }
}
