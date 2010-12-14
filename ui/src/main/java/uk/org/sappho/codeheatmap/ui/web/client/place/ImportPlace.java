package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;

public class ImportPlace extends ProvidedPresenterPlace<ImportPresenter> {

    private final Provider<MainPlace> mainPlaceProvider;
    private final Menu subMenu;

    @Inject
    public ImportPlace(Provider<ImportPresenter> presenter, Provider<MainPlace> mainPlaceProvider, Menu subMenu) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
        this.subMenu = subMenu;
    }

    @Override
    public String getName() {
        return "import";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, ImportPresenter presenter) {
        presenter.bind();
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
        subMenu.clear();
    }

}
