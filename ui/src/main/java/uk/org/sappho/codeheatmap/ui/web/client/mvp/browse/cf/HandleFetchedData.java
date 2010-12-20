package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.inject.Inject;

public class HandleFetchedData extends DispatchCallback<FetchDataResult> {

    private final Display display;

    @Inject
    public HandleFetchedData(CumulativeFlowPresenter.Display display) {
        this.display = display;
    }

    @Override
    public void onSuccess(FetchDataResult result) {
        display.setData(result.getData());
        VisualizationUtils.loadVisualizationApi(display, AreaChart.PACKAGE);
    }

}
