package uk.org.sappho.codeheatmap.ui.web.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.FileParser;
import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.TabDelimitedFileParser;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileParser.class).to(TabDelimitedFileParser.class);
        bind(Database.class).in(Singleton.class);
    }
}
