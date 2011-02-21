package uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view;

public class SubMenuItem {

    private final String caption;
    private final String nameToken;

    public SubMenuItem(String caption, String nameToken) {
        this.caption = caption;
        this.nameToken = nameToken;
    }

    public String getCaption() {
        return caption;
    }

    public String getNameToken() {
        return nameToken;
    }

}
