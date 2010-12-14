package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.client.DispatchAsync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.dispatch.DispatchCallback;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.DataDifferencesHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.DataInfoHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.HeaderNamesComparisonHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.NavigationHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display.Page;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparison;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportData;

@RunWith(MockitoJUnitRunner.class)
public class NavigationHandlerTest {

    @Mock
    private DispatchAsync mockDispatch;
    @Mock
    private ImportPresenter.Display mockDisplay;
    private NavigationHandler handler;

    @Before
    public void setupSut() {
        handler = new NavigationHandler(mockDispatch, mockDisplay);
    }

    @Test
    public void goingToAPageShouldSetTheTitle() {
        handler.gotoPageAfter(Page.UPLOAD_FILE);
        verify(mockDisplay).setPageTitle(anyString());
    }

    @Test
    public void goingToFirstPageShouldSetTheTitle() {
        handler.gotoFirstPage();
        verify(mockDisplay).setPageTitle(anyString());
    }

    @Test
    public void shouldNavigateThroughWizardInTheCorrectOrder() {

        handler.gotoPageAfter(Page.UPLOAD_FILE);
        handler.gotoPageAfter(Page.REVIEW_HEADERS);
        handler.gotoPageAfter(Page.REVIEW_DATA);

        InOrder inOrder = Mockito.inOrder(mockDisplay);
        inOrder.verify(mockDisplay).gotoPage(Page.REVIEW_HEADERS);
        inOrder.verify(mockDisplay).gotoPage(Page.REVIEW_DATA);
        inOrder.verify(mockDisplay).gotoPage(Page.CONFIRM_IMPORT);
    }

    @Test
    public void shouldFetchImportedHeadersWhenNavigatingToReviewHeaders() {
        handler.gotoPageAfter(Page.UPLOAD_FILE);

        verify(mockDispatch).execute(
                isA(FetchHeaderNamesComparison.class),
                isA(HeaderNamesComparisonHandler.class));
    }

    @Test
    public void shouldFetchDataInfoWhenNavigatingToReviewData() {
        handler.gotoPageAfter(Page.REVIEW_HEADERS);

        verify(mockDispatch).execute(isA(FetchDataInfo.class), isA(DataInfoHandler.class));
    }

    @Test
    public void shouldFetchDataDifferencesWhenNavigatingToReviewData() {
        handler.gotoPageAfter(Page.REVIEW_HEADERS);

        verify(mockDispatch).execute(isA(FetchDataDifferences.class), isA(DataDifferencesHandler.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void finishShouldGrabAllTheDataAndSendToTheServer() {
        handler.finish();

        verify(mockDisplay).getHeaderNameMatches();
        verify(mockDispatch).execute(isA(ImportData.class), isA(DispatchCallback.class));
    }
}
