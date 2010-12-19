package uk.org.sappho.codeheatmap.ui.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import uk.org.sappho.codeheatmap.ui.web.client.gin.CodeHeatmapGinjector;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;

public class CodeHeatmapEntryPoint implements EntryPoint {

    private final CodeHeatmapGinjector injector = GWT.create(CodeHeatmapGinjector.class);

    @Override
    public void onModuleLoad() {
        injector.getResources().css().ensureInjected();
        MainPresenter mainPresenter = injector.getMainPresenter();
        mainPresenter.bind();
        RootLayoutPanel.get().add(mainPresenter.getDisplay().asWidget());
        if (!injector.getPlaceManager().fireCurrentPlace()) {
            injector.getMainPlace().goHome();
        }

    }

}
