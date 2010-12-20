package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import net.customware.gwt.dispatch.shared.Action;

public class FetchData implements Action<FetchDataResult> {

    public static final Action<FetchDataResult> CHANGES_BY_RELEASE = new FetchData();

    public FetchData() {
    }
}
