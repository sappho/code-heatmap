package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.ChurnAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.ChurnAnalysisResult;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ChurnDistributionPresenter extends
        Presenter<ChurnDistributionPresenter.MyView, ChurnDistributionPresenter.MyProxy> {

    public static final String nameToken = "ch";

    private final DispatchAsync dispatch;

    @ProxyStandard
    @NameToken(nameToken)
    public interface MyProxy extends Proxy<ChurnDistributionPresenter>, Place {

    }

    public interface MyView extends View, Runnable {

        void clear();

        void setData(List<Integer> list, String worstOffenders);
    }

    @Inject
    public ChurnDistributionPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatch) {
        super(eventBus, view, proxy);
        this.dispatch = dispatch;
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        getView().clear();
        dispatch.execute(new ChurnAnalysis(), new DispatchCallback<ChurnAnalysisResult>() {
            @Override
            public void onSuccess(ChurnAnalysisResult result) {
                getView().setData(result.getFilenameChangeCount(), result.getWorstOffenders());
                VisualizationUtils.loadVisualizationApi(getView(), BarChart.PACKAGE);
            }
        });
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, BrowsePresenter.TYPE_SetBrowseContent, this);
    }

}