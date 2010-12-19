package uk.org.sappho.codeheatmap.ui.web.server.guice;

import com.google.inject.servlet.ServletModule;

import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.FileUploadHandler;

public class FileUploadServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("*.gupld").with(FileUploadHandler.class);
    }
}
