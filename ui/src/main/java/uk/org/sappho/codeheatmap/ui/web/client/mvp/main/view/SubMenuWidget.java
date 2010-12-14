package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.inject.Inject;

public class SubMenuWidget extends Composite {

    @Inject
    public SubMenuWidget() {
        HorizontalPanel subMenu = new HorizontalPanel();
        subMenu.setSize("960px", "1.6em");
        initWidget(subMenu);

    }
}
