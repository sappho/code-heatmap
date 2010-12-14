package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;

public class MainPlace extends ProvidedPresenterPlace<MainPresenter> {

    private final Menu subMenu;

    @Inject
    public MainPlace(Provider<MainPresenter> presenter, Menu subMenu) {
        super(presenter);
        this.subMenu = subMenu;
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
        subMenu.clear();
    }

}
