package uk.org.sappho.codeheatmap.ui.web.client.gin;

import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.gin.AbstractPresenterModule;
import net.customware.gwt.presenter.client.place.ParameterTokenFormatter;
import net.customware.gwt.presenter.client.place.PlaceManager;
import net.customware.gwt.presenter.client.place.TokenFormatter;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.IssuesByReleaseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view.RevisionsByReleaseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.view.BrowseView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.export.ExportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.export.view.ExportView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view.ImportView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.MainView;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.SubMenuWidget;
import uk.org.sappho.codeheatmap.ui.web.client.place.CodeHeatmapPlaceManager;

public class CodeHeatmapModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bind(TokenFormatter.class).to(ParameterTokenFormatter.class);
        bind(PlaceManager.class).to(CodeHeatmapPlaceManager.class).asEagerSingleton();
        bind(EventBus.class).to(DefaultEventBus.class);

        bind(Menu.class).to(SubMenuWidget.class);

        // main menu views
        bindPresenter(MainPresenter.class, MainPresenter.Display.class, MainView.class);
        bindPresenter(ImportPresenter.class, ImportPresenter.Display.class, ImportView.class);
        bindPresenter(ExportPresenter.class, ExportPresenter.Display.class, ExportView.class);
        bindPresenter(BrowsePresenter.class, BrowsePresenter.Display.class, BrowseView.class);

        // browse sub-menu views
        bindPresenter(IssuesByReleasePresenter.class, IssuesByReleasePresenter.Display.class,
                IssuesByReleaseView.class);
        bindPresenter(RevisionsByReleasePresenter.class, RevisionsByReleasePresenter.Display.class,
                RevisionsByReleaseView.class);

    }
}
