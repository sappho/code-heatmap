package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf;

import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

import com.google.inject.Inject;

public class CumulativeFlowPresenter extends WidgetPresenter<CumulativeFlowPresenter.Display> {

    private final DispatchAsync dispatch;
    private final HandleFetchedData handleFetchedData;

    public interface Display extends WidgetDisplay, Runnable {

        void clear();

        void setData(List<DataItem> data);
    }

    @Inject
    public CumulativeFlowPresenter(Display display, EventBus eventBus, DispatchAsync dispatch,
            HandleFetchedData handleFetchedData) {
        super(display, eventBus);
        this.dispatch = dispatch;
        this.handleFetchedData = handleFetchedData;
    }

    @Override
    protected void onRevealDisplay() {
        display.clear();
        dispatch.execute(FetchData.CHANGES_BY_RELEASE, handleFetchedData);
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onUnbind() {

    }

}