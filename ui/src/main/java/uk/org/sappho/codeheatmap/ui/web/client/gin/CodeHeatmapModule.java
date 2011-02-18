package uk.org.sappho.codeheatmap.ui.web.client.gin;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.BadCommittersPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.ChurnDistributionPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.TopChurnByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.BadCommittersView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.ChurnDistributionView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.IssuesByReleaseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.RevisionsByReleaseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.TopChurnByReleaseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.view.BrowseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.MainView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.SubMenuWidget;
import uk.org.sappho.codeheatmap.ui.web.client.place.CodeHeatmapPlaceManager;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class CodeHeatmapModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(PlaceManager.class).to(CodeHeatmapPlaceManager.class).in(Singleton.class);
        bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        bind(RootPresenter.class).asEagerSingleton();
        bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);

        bind(Menu.class).to(SubMenuWidget.class);

        // main menu views
        bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
                MainView.class, MainPresenter.MyProxy.class);

        bindPresenter(BrowsePresenter.class, BrowsePresenter.MyView.class,
                BrowseView.class, BrowsePresenter.MyProxy.class);

        // browse sub-menu views
        bindPresenter(IssuesByReleasePresenter.class, IssuesByReleasePresenter.MyView.class,
                IssuesByReleaseView.class, IssuesByReleasePresenter.MyProxy.class);
        bindPresenter(RevisionsByReleasePresenter.class, RevisionsByReleasePresenter.MyView.class,
                RevisionsByReleaseView.class, RevisionsByReleasePresenter.MyProxy.class);
        bindPresenter(ChurnDistributionPresenter.class, ChurnDistributionPresenter.MyView.class,
                ChurnDistributionView.class, ChurnDistributionPresenter.MyProxy.class);
        bindPresenter(BadCommittersPresenter.class, BadCommittersPresenter.MyView.class,
                BadCommittersView.class, BadCommittersPresenter.MyProxy.class);
        bindPresenter(TopChurnByReleasePresenter.class, TopChurnByReleasePresenter.MyView.class,
                TopChurnByReleaseView.class, TopChurnByReleasePresenter.MyProxy.class);

    }
}
