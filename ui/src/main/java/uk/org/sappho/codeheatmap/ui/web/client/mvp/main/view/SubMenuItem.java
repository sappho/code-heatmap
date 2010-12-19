package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

import uk.org.sappho.codeheatmap.ui.web.client.place.SubMenuPlace;

public class SubMenuItem {

    private final String caption;
    private final SubMenuPlace<?> subMenuplace;

    public SubMenuItem(String caption, SubMenuPlace<?> subMenuPlace) {
        this.caption = caption;
        this.subMenuplace = subMenuPlace;
    }

    public String getCaption() {
        return caption;
    }

    public SubMenuPlace<?> getPlace() {
        return subMenuplace;
    }
}
