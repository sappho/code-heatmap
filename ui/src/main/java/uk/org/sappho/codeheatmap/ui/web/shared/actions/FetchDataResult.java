package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

public class FetchDataResult implements Result {

    private List<DataItem> data;

    public FetchDataResult() {
    }

    public FetchDataResult(List<DataItem> data) {
        this.data = data;
    }

    public List<DataItem> getData() {
        return data;
    }

}
