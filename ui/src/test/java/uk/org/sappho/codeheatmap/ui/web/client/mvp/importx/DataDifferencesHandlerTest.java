package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.DataDifferencesHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataDifferencesResult;

@RunWith(MockitoJUnitRunner.class)
public class DataDifferencesHandlerTest {

    @Mock
    private ImportPresenter.Display mockDisplay;

    @Test
    public void shouldPassDataDifferencesToTheDisplayForRendering() {
        DataDifferencesHandler dataDifferencesHandler = new DataDifferencesHandler(mockDisplay);
        FetchDataDifferencesResult result = new FetchDataDifferencesResult(null);
        dataDifferencesHandler.onSuccess(result);
        verify(mockDisplay).setDataDifferences(result.getDataDifferences());
        verifyNoMoreInteractions(mockDisplay);
    }

}
