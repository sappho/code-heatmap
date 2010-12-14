package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparisonResult;

public class HeaderNamesComparisonHandler extends DispatchCallback<FetchHeaderNamesComparisonResult> {

    private final Display display;

    public HeaderNamesComparisonHandler(ImportPresenter.Display display) {
        this.display = display;
    }

    @Override
    public void onSuccess(FetchHeaderNamesComparisonResult result) {
        display.setHeaderNameMatches(result.getHeaderMatches());
    }
}
