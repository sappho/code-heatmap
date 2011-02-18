package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class FetchData extends UnsecuredActionImpl<FetchDataResult> {

    public static final Action<FetchDataResult> ISSUES_BY_RELEASE = new FetchData(FetchDataType.ISSUES);
    public static final Action<FetchDataResult> REVISIONS_BY_RELEASE = new FetchData(FetchDataType.REVISIONS);
    public static final Action<FetchDataResult> CHURN = new FetchData(FetchDataType.CHURN);

    private FetchDataType fetchDataType;

    public enum FetchDataType {
        ISSUES,
        REVISIONS,
        CHURN
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
