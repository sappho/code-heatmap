package uk.org.sappho.codeheatmap.ui.web.client.mvp.main;

import static org.mockito.Mockito.verify;
import net.customware.gwt.presenter.client.EventBus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    private MainPresenter.Display mockDisplay;
    @Mock
    private EventBus mockEventBus;

    @Test
    public void shouldUpdateDisplayWithContent() {
        new MainPresenter(mockDisplay, mockEventBus).setContent(null);
        verify(mockDisplay).setContent(null);
    }
}
