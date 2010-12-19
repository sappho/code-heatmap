package uk.org.sappho.codeheatmap.ui.web.client.place;

import net.customware.gwt.presenter.client.gin.ProvidedPresenterPlace;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class SubMenuPlace<T extends WidgetPresenter<?>> extends ProvidedPresenterPlace<T> {

    @Inject
    public SubMenuPlace(Provider<T> presenter) {
        super(presenter);
    }

    protected String getPrefix() {
        return "sub-";
    }

}
