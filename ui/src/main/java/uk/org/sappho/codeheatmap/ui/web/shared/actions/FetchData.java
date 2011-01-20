package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import net.customware.gwt.dispatch.shared.Action;

public class FetchData implements Action<FetchDataResult> {

    public static final Action<FetchDataResult> ISSUES_BY_RELEASE = new FetchData(FetchDataType.ISSUES);
    public static final Action<FetchDataResult> REVISIONS_BY_RELEASE = new FetchData(FetchDataType.REVISIONS);
    private FetchDataType fetchDataType;

    public enum FetchDataType {
        ISSUES,
        REVISIONS
    }

    public FetchData() {
    }

    public FetchData(FetchDataType fetchDataType) {
        this.fetchDataType = fetchDataType;
    }

    public FetchDataType getFetchDataType() {
        return fetchDataType;
    }

}
