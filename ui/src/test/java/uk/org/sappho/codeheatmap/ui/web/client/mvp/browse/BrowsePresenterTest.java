package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;

@RunWith(MockitoJUnitRunner.class)
public class BrowsePresenterTest {

    @Mock
    private BrowsePresenter.Display mockDisplay;
    @Mock
    private EventBus mockEventBus;
    @Mock
    private DispatchAsync mockDispatch;

    private BrowsePresenter presenter;

    @Before
    public void setupSut() {
        presenter = new BrowsePresenter(mockDisplay, mockEventBus, mockDispatch);
    }

    @Test
    public void shouldBindToSomething() {
        presenter.bind();
    }

    @Test
    public void shouldBindToSearchTermChanges() {
        presenter.bind();

        verify(mockDisplay).addSearchTermChangeHandler(isA(SearchCriteriaChangeEvent.Handler.class));
    }

}
