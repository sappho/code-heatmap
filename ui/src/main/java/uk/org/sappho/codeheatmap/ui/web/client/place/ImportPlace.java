package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.place.PlaceRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;

public class ImportPlace extends ProvidedPresenterPlace<ImportPresenter> {

    private final Provider<MainPlace> mainPlaceProvider;

    @Inject
    public ImportPlace(Provider<ImportPresenter> presenter, Provider<MainPlace> mainPlaceProvider) {
        super(presenter);
        this.mainPlaceProvider = mainPlaceProvider;
    }

    @Override
    public String getName() {
        return "import";
    }

    @Override
    protected void preparePresenter(PlaceRequest request, ImportPresenter presenter) {
        presenter.bind();
        mainPlaceProvider.get().getPresenter().setContent(presenter.getDisplay().asWidget());
    }

}
