package uk.org.sappho.codeheatmap.ui.web.server.guice;

import org.junit.Test;

import com.google.inject.Guice;

import uk.org.sappho.codeheatmap.ui.web.server.guice.HandlersModule;
import uk.org.sappho.codeheatmap.ui.web.server.guice.ServiceModule;

public class ServiceModuleTest {

    @Test
    public void shouldWire() {
        Guice.createInjector(new ServiceModule(), new HandlersModule());
    }
}
