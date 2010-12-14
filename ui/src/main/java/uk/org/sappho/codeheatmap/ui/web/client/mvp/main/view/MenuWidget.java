package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.place.BrowsePlace;
import uk.org.sappho.codeheatmap.ui.web.client.place.ExportPlace;
import uk.org.sappho.codeheatmap.ui.web.client.place.ImportPlace;
import uk.org.sappho.codeheatmap.ui.web.client.place.MainPlace;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

public class MenuWidget extends Composite {

    @Inject
    public MenuWidget(CodeHeatmapResources resources,
            MainPlace mainPlace,
            ImportPlace importPlace,
            ExportPlace exportPlace,
            BrowsePlace browsePlace) {

        HorizontalPanel menu = new HorizontalPanel();
        menu.setSize("960px", "3.4em");
        menu.setSpacing(8);
        menu.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        Label logo = new Label("Code Heatmap");
        logo.addStyleName(resources.css().logo());
        menu.add(logo);
        menu.setCellWidth(logo, "100%");
        menu.add(new Hyperlink("Main", mainPlace.getName()));
        menu.add(new Hyperlink("Import", importPlace.getName()));
        menu.add(new Hyperlink("Export", exportPlace.getName()));
        menu.add(new Hyperlink("Browse", browsePlace.getName()));
        initWidget(menu);
    }
}
