package uk.org.sappho.codeheatmap.ui.web.server.guice;

import org.junit.Test;

import com.google.inject.Guice;

public class ServiceModuleTest {

    @Test
    public void shouldWire() {
        Guice.createInjector(new ServiceModule(), new HandlersModule());
    }
}
