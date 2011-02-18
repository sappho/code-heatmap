package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse;

import java.util.Collection;

import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.BadCommittersPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.ChurnDistributionPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.TopChurnByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.SubMenuItem;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class BrowsePresenter extends Presenter<BrowsePresenter.MyView, BrowsePresenter.MyProxy> {

    public static final String nameToken = "browse";

    private final Menu subMenu;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetBrowseContent = new Type<RevealContentHandler<?>>();

    public interface MyView extends View {
        void setData(Collection<String> data);

        void addSearchTermChangeHandler(SearchCriteriaChangeEvent.Handler searchCriteriaChangeHandler);
    }

    @ProxyStandard
    @NameToken(nameToken)
    public interface MyProxy extends Proxy<BrowsePresenter>, Place {

    }

    @Inject
    public BrowsePresenter(EventBus eventBus, MyView view, MyProxy proxy, Menu subMenu) {
        super(eventBus, view, proxy);
        this.subMenu = subMenu;
    }

    private void runSearch(String searchTerm) {
    }

    @Override
    protected void onBind() {
        getView().addSearchTermChangeHandler(new SearchChangeHandler());
    }

    private final class SearchChangeHandler implements SearchCriteriaChangeEvent.Handler {
        @Override
        public void onSearchCriteriaChanged(SearchCriteriaChangeEvent event) {
            runSearch(event.getSearchTerm());
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        subMenu.clear();
        subMenu.addMenuItem(new SubMenuItem("Issues/Release", IssuesByReleasePresenter.nameToken));
        subMenu.addMenuItem(new SubMenuItem("Revision/Release", RevisionsByReleasePresenter.nameToken));
        subMenu.addMenuItem(new SubMenuItem("Churn Distribution", ChurnDistributionPresenter.nameToken));
        subMenu.addMenuItem(new SubMenuItem("Bad Committers", BadCommittersPresenter.nameToken));
        subMenu.addMenuItem(new SubMenuItem("Top Churn By Release", TopChurnByReleasePresenter.nameToken));
    }

    @Override
    protected void revealInParent() {
        runSearch("");
        RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
    }

}