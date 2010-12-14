package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.DataInfoHandler;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfoResult;

@RunWith(MockitoJUnitRunner.class)
public class DataInfoHandlerTest {

    @Mock
    private ImportPresenter.Display mockDisplay;

    @Test
    public void shouldPassDataInfoToTheDisplayForRendering() {
        DataInfoHandler dataInfoHandler = new DataInfoHandler(mockDisplay);
        FetchDataInfoResult result = new FetchDataInfoResult(null);
        dataInfoHandler.onSuccess(result);
        verify(mockDisplay).setDataInfo(result.getDataInfo());
        verifyNoMoreInteractions(mockDisplay);
    }

}
