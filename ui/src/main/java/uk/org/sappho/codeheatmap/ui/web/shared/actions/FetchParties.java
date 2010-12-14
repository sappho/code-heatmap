package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import net.customware.gwt.dispatch.shared.Action;

public class FetchParties implements Action<FetchPartiesResult> {

    private static final long serialVersionUID = -865229682537751479L;
    private String searchTerm;

    public FetchParties() {
    }

    public FetchParties(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

}
