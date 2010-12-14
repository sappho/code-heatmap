package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.HeaderNamesComparisonHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchHeaderNamesComparisonResult;

@RunWith(MockitoJUnitRunner.class)
public class HeaderNamesComparisonHandlerTest {

    @Mock
    private ImportPresenter.Display mockDisplay;

    @Test
    public void shouldPassHeaderListToTheDisplayForRendering() {
        HeaderNamesComparisonHandler handleHeaders = new HeaderNamesComparisonHandler(mockDisplay);
        FetchHeaderNamesComparisonResult result = new FetchHeaderNamesComparisonResult(null);
        handleHeaders.onSuccess(result);
        verify(mockDisplay).setHeaderNameMatches(result.getHeaderMatches());
        verifyNoMoreInteractions(mockDisplay);
    }

}
