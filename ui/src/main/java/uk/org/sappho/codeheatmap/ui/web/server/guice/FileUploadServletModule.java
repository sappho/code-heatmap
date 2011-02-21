package uk.org.sappho.codeheatmap.ui.web.server.guice;

import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.FileUploadHandler;

import com.google.inject.servlet.ServletModule;

public class FileUploadServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("*.gupld").with(FileUploadHandler.class);
    }
}
