package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SubMenuWidget extends Composite implements Menu {

    private final HorizontalPanel subMenu;

    @Inject
    public SubMenuWidget() {
        subMenu = new HorizontalPanel();
        subMenu.setSize("960px", "1.6em");
        initWidget(subMenu);
    }

    private void addSpacer(HorizontalPanel subMenu) {
        Label spacer = new Label("");
        subMenu.add(spacer); // spacer
        subMenu.setCellWidth(spacer, "100%");
    }

    @Override
    public void clear() {
        subMenu.clear();
        addSpacer(subMenu);
    }

    @Override
    public void addMenuItem(String... menuItems) {
        for (String menuItem : menuItems) {
            subMenu.add(new Hyperlink(menuItem, "sub-menu-url"));
        }
    }
}
