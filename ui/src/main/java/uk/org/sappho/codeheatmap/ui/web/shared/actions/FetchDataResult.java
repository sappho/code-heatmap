package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;


import com.gwtplatform.dispatch.shared.Result;

public class FetchDataResult implements Result {

    private List<ReleaseChangesDefects> data;

    public FetchDataResult() {
    }

    public FetchDataResult(List<ReleaseChangesDefects> data) {
        this.data = data;
    }

    public List<ReleaseChangesDefects> getData() {
        return data;
    }

}
