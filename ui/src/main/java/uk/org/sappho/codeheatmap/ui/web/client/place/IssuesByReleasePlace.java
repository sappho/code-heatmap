package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;

public class IssuesByReleasePlace extends SubMenuPlace<IssuesByReleasePresenter> {

    private final Provider<MainPlace> mainPlaceProvider;

    @Inject
    public IssuesByReleasePlace(Provider<IssuesByReleasePresenter> presenter, Provider<MainPlace> mainPlaceProvider) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
    }

    @Override
    public String getName() {
        return getPrefix() + "ibr";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, IssuesByReleasePresenter presenter) {
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
    }
}
