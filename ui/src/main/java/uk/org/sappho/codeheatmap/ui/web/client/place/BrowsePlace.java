package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;

public class BrowsePlace extends ProvidedPresenterPlace<BrowsePresenter> {

    private final Provider<MainPlace> mainPlaceProvider;
    private final Menu subMenu;

    @Inject
    public BrowsePlace(Provider<BrowsePresenter> presenter,
            Provider<MainPlace> mainPlaceProvider,
            Menu subMenu) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
        this.subMenu = subMenu;
    }

    @Override
    public String getName() {
        return "browse";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, BrowsePresenter presenter) {
        presenter.bind();
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
        subMenu.clear();
        subMenu.addMenuItem("Cumulative Value");
    }

}
