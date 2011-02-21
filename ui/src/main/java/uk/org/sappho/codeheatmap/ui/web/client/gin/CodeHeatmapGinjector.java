package uk.org.sappho.codeheatmap.ui.web.client.gin;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.BadCommittersPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.ChurnDistributionPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.TopChurnByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({ DispatchAsyncModule.class, CodeHeatmapModule.class })
public interface CodeHeatmapGinjector extends Ginjector {

    EventBus getEventBus();

    ProxyFailureHandler getProxyFailureHandler();

    PlaceManager getPlaceManager();

    CodeHeatmapResources getResources();

    Provider<MainPresenter> getMainPresenter();

    Provider<BrowsePresenter> getBrowsePresenter();

    // charts
    Provider<IssuesByReleasePresenter> getIBRPresenter();

    Provider<RevisionsByReleasePresenter> getRBRPresenter();

    Provider<ChurnDistributionPresenter> getChurnDistributionPresenter();

    Provider<BadCommittersPresenter> getBadCommittersPresenter();

    Provider<TopChurnByReleasePresenter> getTopChurnByReleasePresenter();

}
