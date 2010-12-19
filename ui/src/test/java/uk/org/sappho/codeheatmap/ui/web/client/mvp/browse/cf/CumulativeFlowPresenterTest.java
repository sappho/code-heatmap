package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

@RunWith(MockitoJUnitRunner.class)
public class CumulativeFlowPresenterTest {

    @Mock
    private EventBus mockEventBus;
    @Mock
    private Display mockDisplay;
    @Mock
    private DispatchAsync mockDispatch;
    @Mock
    private HandleFetchedData mockHandleFetchedData;
    private CumulativeFlowPresenter cumulativeFlowPresenter;

    @Before
    public void setup() {
        cumulativeFlowPresenter = new CumulativeFlowPresenter(mockDisplay, mockEventBus, mockDispatch,
                mockHandleFetchedData);
    }

    @Test
    public void shouldFetchDataOnReveal() {
        cumulativeFlowPresenter.onRevealDisplay();
        verify(mockDispatch).execute(isA(FetchData.class), isA(HandleFetchedData.class));
    }
}
