package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

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

public class IssuesByReleasePresenter extends
        Presenter<IssuesByReleasePresenter.MyView, IssuesByReleasePresenter.MyProxy> {

    public static final String nameToken = "ibr";

    private final DispatchAsync dispatch;
    private final HandleDataForIssuesByRelease handleDataForIssuesByRelease;

    @ProxyStandard
    @NameToken(nameToken)
    public interface MyProxy extends Proxy<IssuesByReleasePresenter>, Place {

    }

    public interface MyView extends View, Runnable {

        void clear();

        void setData(List<DataItem> data);
    }

    @Inject
    public IssuesByReleasePresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatch,
            HandleDataForIssuesByRelease handleDataForIssuesByRelease) {
        super(eventBus, view, proxy);
        this.dispatch = dispatch;
        this.handleDataForIssuesByRelease = handleDataForIssuesByRelease;
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, BrowsePresenter.TYPE_SetBrowseContent, this);
        getView().clear();
        dispatch.execute(FetchData.ISSUES_BY_RELEASE, handleDataForIssuesByRelease);
    }

}