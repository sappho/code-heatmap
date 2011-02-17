package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class MenuWidget extends Composite {

    @Inject
    public MenuWidget(CodeHeatmapResources resources) {

        HorizontalPanel menu = new HorizontalPanel();
        menu.setSize("960px", "3.4em");
        menu.setSpacing(8);
        menu.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        Label logo = new Label("Code Heatmap");
        logo.addStyleName(resources.css().logo());
        menu.add(logo);
        menu.setCellWidth(logo, "100%");
        menu.add(new Hyperlink("Main", MainPresenter.nameToken));
        menu.add(new Hyperlink("Browse", BrowsePresenter.nameToken));
        initWidget(menu);
    }
}
