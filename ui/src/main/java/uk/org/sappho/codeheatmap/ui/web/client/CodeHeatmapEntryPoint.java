package uk.org.sappho.codeheatmap.ui.web.client;

import uk.org.sappho.codeheatmap.ui.web.client.gin.CodeHeatmapGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

public class CodeHeatmapEntryPoint implements EntryPoint {

    private final CodeHeatmapGinjector injector = GWT.create(CodeHeatmapGinjector.class);

    @Override
    public void onModuleLoad() {

        injector.getResources().css().ensureInjected();

        DelayedBindRegistry.bind(injector);

        injector.getPlaceManager().revealCurrentPlace();

    }

}
