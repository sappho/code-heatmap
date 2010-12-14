package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;

public class BrowsePlace extends ProvidedPresenterPlace<BrowsePresenter> {

    private final Provider<MainPlace> mainPlaceProvider;

    @Inject
    public BrowsePlace(Provider<BrowsePresenter> presenter,
            Provider<MainPlace> mainPlaceProvider) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
    }

    @Override
    public String getName() {
        return "browse";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, BrowsePresenter presenter) {
        presenter.bind();
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
    }

}
