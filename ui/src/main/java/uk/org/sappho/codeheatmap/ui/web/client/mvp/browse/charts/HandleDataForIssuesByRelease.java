package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;

public class HandleDataForIssuesByRelease extends DispatchCallback<FetchDataResult> {

    private final IssuesByReleasePresenter.Display display;

    @Inject
    public HandleDataForIssuesByRelease(IssuesByReleasePresenter.Display display) {
        this.display = display;
    }

    @Override
    public void onSuccess(FetchDataResult result) {
        display.setData(result.getData());
        VisualizationUtils.loadVisualizationApi(display, AreaChart.PACKAGE);
    }

}
