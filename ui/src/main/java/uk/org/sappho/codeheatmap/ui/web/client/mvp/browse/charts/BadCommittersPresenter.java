package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.BadCommittersAnalysis;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.BadCommittersAnalysisResult;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.CommitterStats;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class BadCommittersPresenter extends
        Presenter<BadCommittersPresenter.MyView, BadCommittersPresenter.MyProxy> {

    public static final String nameToken = "bc";

    private final DispatchAsync dispatch;

    @ProxyStandard
    @NameToken(nameToken)
    public interface MyProxy extends Proxy<BadCommittersPresenter>, Place {

    }

    public interface MyView extends View {

        void setData(List<CommitterStats> data);
    }

    @Inject
    public BadCommittersPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatch) {
        super(eventBus, view, proxy);
        this.dispatch = dispatch;
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        dispatch.execute(new BadCommittersAnalysis(), new DispatchCallback<BadCommittersAnalysisResult>() {
            @Override
            public void onSuccess(BadCommittersAnalysisResult result) {
                getView().setData(result.getData());
            }
        });
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, BrowsePresenter.TYPE_SetBrowseContent, this);
    }

}