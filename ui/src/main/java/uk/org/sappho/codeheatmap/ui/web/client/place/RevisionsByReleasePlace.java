package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;

public class RevisionsByReleasePlace extends SubMenuPlace<RevisionsByReleasePresenter> {

    private final Provider<MainPlace> mainPlaceProvider;

    @Inject
    public RevisionsByReleasePlace(Provider<RevisionsByReleasePresenter> presenter,
            Provider<MainPlace> mainPlaceProvider) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
    }

    @Override
    public String getName() {
        return getPrefix() + "rbr";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, RevisionsByReleasePresenter presenter) {
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
    }
}
