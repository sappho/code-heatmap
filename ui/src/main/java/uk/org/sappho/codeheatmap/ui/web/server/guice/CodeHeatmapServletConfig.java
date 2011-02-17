package uk.org.sappho.codeheatmap.ui.web.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class CodeHeatmapServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new DispatchServletModule(),
                new FileUploadServletModule(),
                new HandlersModule(),
                new ServiceModule());
    }

}
