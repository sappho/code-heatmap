package uk.org.sappho.codeheatmap.ui.web.client.gin;

import net.customware.gwt.dispatch.client.gin.StandardDispatchModule;
import net.customware.gwt.presenter.client.place.PlaceManager;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.place.MainPlace;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

@GinModules({ CodeHeatmapModule.class, StandardDispatchModule.class })
public interface CodeHeatmapGinjector extends Ginjector {

    MainPlace getMainPlace();

    MainPresenter getMainPresenter();

    PlaceManager getPlaceManager();

    CodeHeatmapResources getResources();

}
