package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.shared.model.DataInfo;

import net.customware.gwt.dispatch.shared.Result;

public class FetchDataInfoResult implements Result {

    private List<DataInfo> dataInfo;

    public FetchDataInfoResult() {
    }

    public FetchDataInfoResult(List<DataInfo> dataInfo) {
        this.dataInfo = dataInfo;
    }

    public List<DataInfo> getDataInfo() {
        return dataInfo;
    }

}
