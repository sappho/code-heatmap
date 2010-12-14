package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.DefaultPlaceManager;
import net.customware.gwt.presenter.client.place.TokenFormatter;

import com.google.inject.Inject;

public class CodeHeatmapPlaceManager extends DefaultPlaceManager {

    @Inject
    public CodeHeatmapPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter,
            MainPlace mainPlace, ImportPlace importPlace,
            ExportPlace exportPlace,
            BrowsePlace browsePlace) {
        super(eventBus, tokenFormatter, mainPlace, importPlace, exportPlace, browsePlace);
    }

}
