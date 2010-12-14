package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.server.handlers.FetchDataDifferencesHandler;
import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.AnalyseDifferences;

@RunWith(MockitoJUnitRunner.class)
public class FetchDataDifferencesHandlerTest {

    @Mock
    private AnalyseDifferences mockAnalyseDifferences;

    @Test
    public void shouldDelegateResponsibilityForDiffingParties() throws DispatchException {

        FetchDataDifferencesHandler fetchDataDifferencesHandler = new FetchDataDifferencesHandler(
                mockAnalyseDifferences);
        fetchDataDifferencesHandler.execute(null, null);

        verify(mockAnalyseDifferences).analyse();
        verify(mockAnalyseDifferences).getDifferences();

    }
}
