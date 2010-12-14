package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfoResult;

public class DataInfoHandler extends DispatchCallback<FetchDataInfoResult> {

    private final Display display;

    public DataInfoHandler(Display display) {
        this.display = display;
    }

    @Override
    public void onSuccess(FetchDataInfoResult result) {
        display.setDataInfo(result.getDataInfo());
    }

}
