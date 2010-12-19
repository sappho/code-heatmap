package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.client.DispatchAsync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter.Display.Page;

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

}
