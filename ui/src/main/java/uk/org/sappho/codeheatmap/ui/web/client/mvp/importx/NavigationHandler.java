package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display.Page;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparison;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportDataResult;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

public class NavigationHandler {
    private final Display display;
    private final DispatchAsync dispatch;

    @Inject
    public NavigationHandler(DispatchAsync dispatch, ImportPresenter.Display display) {
        this.dispatch = dispatch;
        this.display = display;
    }

    public void gotoPageAfter(Page thisPage) {
        Page targetPage = pageAfter(thisPage);
        setPageTitle(targetPage);
        handlePagePopulation(targetPage);
        display.gotoPage(targetPage);
    }

    private void setPageTitle(Page targetPage) {
        switch (targetPage) {
        case UPLOAD_FILE:
            display.setPageTitle("Upload File");
            break;
        case REVIEW_HEADERS:
            display.setPageTitle("Review Headers");
            break;
        case REVIEW_DATA:
            display.setPageTitle("Review Data");
            break;
        case CONFIRM_IMPORT:
            display.setPageTitle("Confirm");
            break;
        default:
            break;
        }
    }

    private void handlePagePopulation(Page targetPage) {
        if (targetPage == Page.REVIEW_HEADERS) {
            dispatch.execute(new FetchHeaderNamesComparison(), new HeaderNamesComparisonHandler(display));
        }
        if (targetPage == Page.REVIEW_DATA) {
            dispatch.execute(new FetchDataInfo(), new DataInfoHandler(display));
            dispatch.execute(new FetchDataDifferences(), new DataDifferencesHandler(display));
        }
    }

    private Page pageAfter(Page previousPage) {
        return Page.values()[previousPage.ordinal() + 1];
    }

    public void gotoFirstPage() {
        setPageTitle(Page.UPLOAD_FILE);
        display.gotoPage(Page.values()[0]);
    }

    public void finish() {
        List<HeaderMatch> headerNameMatches = new ArrayList<HeaderMatch>(display.getHeaderNameMatches());
        dispatch.execute(new ImportData(headerNameMatches), new DispatchCallback<ImportDataResult>() {
            @Override
            public void onSuccess(ImportDataResult result) {
            }
        });
    }
}