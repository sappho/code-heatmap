package uk.org.sappho.codeheatmap.ui.web.client.place;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class CodeHeatmapPlaceManager extends PlaceManagerImpl {

    @Inject
    public CodeHeatmapPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter) {
        super(eventBus, tokenFormatter);
    }

    @Override
    public void revealDefaultPlace() {
        revealPlace(new PlaceRequest(MainPresenter.nameToken));
    }

}
