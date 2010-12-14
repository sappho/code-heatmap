package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;

public class SubMenuPlace extends ProvidedPresenterPlace<BrowsePresenter> {

    @Inject
    public SubMenuPlace(Provider<BrowsePresenter> presenter) {
        super(presenter);
    }

    @Override
    public String getName() {
        return null;
    }

}
