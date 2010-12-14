package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.place.PlaceRequest;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CumulativeFlowPlace extends SubMenuPlace<CumulativeFlowPresenter> {

    private final Provider<MainPlace> mainPlaceProvider;

    @Inject
    public CumulativeFlowPlace(Provider<CumulativeFlowPresenter> presenter, Provider<MainPlace> mainPlaceProvider) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
    }

    @Override
    public String getName() {
        return getPrefix() + "cf";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, CumulativeFlowPresenter presenter) {
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
    }
}
