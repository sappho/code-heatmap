package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

public class RevisionsByReleasePresenter extends WidgetPresenter<RevisionsByReleasePresenter.Display> {

    private final DispatchAsync dispatch;
    private final HandleDataForRevisionsByRelease handleDataForRevisionsByRelease;

    public interface Display extends WidgetDisplay, Runnable {

        void clear();

        void setData(List<DataItem> data);
    }

    @Inject
    public RevisionsByReleasePresenter(Display display, EventBus eventBus, DispatchAsync dispatch,
            HandleDataForRevisionsByRelease handleDataForRevisionsByRelease) {
        super(display, eventBus);
        this.dispatch = dispatch;
        this.handleDataForRevisionsByRelease = handleDataForRevisionsByRelease;
    }

    @Override
    protected void onRevealDisplay() {
        display.clear();
        dispatch.execute(FetchData.REVISIONS_BY_RELEASE, handleDataForRevisionsByRelease);
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onUnbind() {

    }

}