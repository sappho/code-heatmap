package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;

public class MainPlace extends ProvidedPresenterPlace<MainPresenter> {

    @Inject
    public MainPlace(Provider<MainPresenter> presenter) {
        super(presenter);
    }

    @Override
    public String getName() {
        return "main";
    }

    public void goHome() {
        getPresenter().setContent(new Label("No bookmark selected, you defaulted to this page."));
    }

    @Override
    protected void preparePresenter(PlaceRequest request, MainPresenter presenter) {
        getPresenter().setContent(new Label("Main Page Content"));
    }

}
