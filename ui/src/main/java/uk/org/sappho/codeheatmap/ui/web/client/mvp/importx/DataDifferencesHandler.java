package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferencesResult;

public class DataDifferencesHandler extends DispatchCallback<FetchDataDifferencesResult> {

    private final Display display;

    public DataDifferencesHandler(Display display) {
        this.display = display;
    }

    @Override
    public void onSuccess(FetchDataDifferencesResult result) {
        display.setDataDifferences(result.getDataDifferences());
    }

}
