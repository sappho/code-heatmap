package uk.org.sappho.codeheatmap.ui.web.client.mvp.export;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.inject.Inject;

public class ExportPresenter extends WidgetPresenter<ExportPresenter.Display> {

    public interface Display extends WidgetDisplay {

    }

    @Inject
    public ExportPresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
    }

    @Override
    protected void onRevealDisplay() {
    }

    @Override
    protected void onBind() {

    }

    @Override
    protected void onUnbind() {

    }
}
