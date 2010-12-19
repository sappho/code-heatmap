package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

public class MainView extends Composite implements MainPresenter.Display {

    private final ScrollPanel contentWrapper;

    @Inject
    public MainView(CodeHeatmapResources resources, MenuWidget mainMenu, SubMenuWidget subMenuWidget) {

        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);

        HorizontalPanel menuWrapper = new HorizontalPanel();
        menuWrapper.setWidth("100%");
        menuWrapper.addStyleName(resources.css().centerLayout());
        menuWrapper.addStyleName(resources.css().menuContainer());
        menuWrapper.add(mainMenu);
        menuWrapper.setCellHorizontalAlignment(mainMenu, HorizontalPanel.ALIGN_CENTER);
        dockLayoutPanel.addNorth(menuWrapper, 3.4d);

        HorizontalPanel submenuWrapper = new HorizontalPanel();
        submenuWrapper.setWidth("100%");
        submenuWrapper.addStyleName(resources.css().subMenuContainer());
        submenuWrapper.add(subMenuWidget);
        submenuWrapper.setCellHorizontalAlignment(subMenuWidget, HorizontalPanel.ALIGN_CENTER);
        dockLayoutPanel.addNorth(submenuWrapper, 1.6d);

        HorizontalPanel footer = new HorizontalPanel();
        footer.setSize("100%", "100%");
        footer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        footer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        footer.addStyleName(resources.css().footer());
        footer.add(new HTML("Code Heatmap"));
        dockLayoutPanel.addSouth(footer, 1.6d);

        contentWrapper = new ScrollPanel();
        contentWrapper.setWidth("100%");
        contentWrapper.addStyleName(resources.css().content());
        dockLayoutPanel.add(contentWrapper);

        initWidget(dockLayoutPanel);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setContent(Widget content) {
        contentWrapper.setWidget(content);
    }
}
