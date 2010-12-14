package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.inject.Inject;

public class CumulativeFlowPresenter extends WidgetPresenter<CumulativeFlowPresenter.Display> {

    public interface Display extends WidgetDisplay {
    }

    @Inject
    public CumulativeFlowPresenter(Display display, EventBus eventBus) {
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