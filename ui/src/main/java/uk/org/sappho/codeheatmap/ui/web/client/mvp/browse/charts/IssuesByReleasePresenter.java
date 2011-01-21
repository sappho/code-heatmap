package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

import com.google.inject.Inject;

public class IssuesByReleasePresenter extends WidgetPresenter<IssuesByReleasePresenter.Display> {

    private final DispatchAsync dispatch;
    private final HandleDataForIssuesByRelease handleDataForIssuesByRelease;

    public interface Display extends WidgetDisplay, Runnable {

        void clear();

        void setData(List<DataItem> data);
    }

    @Inject
    public IssuesByReleasePresenter(Display display, EventBus eventBus, DispatchAsync dispatch,
            HandleDataForIssuesByRelease handleDataForIssuesByRelease) {
        super(display, eventBus);
        this.dispatch = dispatch;
        this.handleDataForIssuesByRelease = handleDataForIssuesByRelease;
    }

    @Override
    protected void onRevealDisplay() {
        display.clear();
        dispatch.execute(FetchData.ISSUES_BY_RELEASE, handleDataForIssuesByRelease);
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onUnbind() {

    }

}