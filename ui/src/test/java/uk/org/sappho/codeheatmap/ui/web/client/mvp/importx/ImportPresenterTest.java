package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import static org.mockito.Mockito.verify;
import net.customware.gwt.presenter.client.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ImportPresenterTest {

    @Mock
    private ImportPresenter.Display mockDisplay;
    @Mock
    private EventBus mockEventBus;
    @Mock
    private NavigationHandler mockNavigationHandler;
    private ImportPresenter importPresenter;

    @Before
    public void setupSut() {
        importPresenter = new ImportPresenter(mockDisplay, mockEventBus, mockNavigationHandler);
    }

    @Test
    public void shouldStartAtFileUploadPageOnReveal() {
        importPresenter.revealDisplay();
        verify(mockNavigationHandler).gotoFirstPage();
    }

    @Test
    public void shouldBindToNextButton() {
        importPresenter.bind();
        verify(mockDisplay).bindToNextButton(mockNavigationHandler);
    }

    @Test
    public void shouldBindToFinishButton() {
        importPresenter.bind();
        verify(mockDisplay).bindToFinishButton(mockNavigationHandler);
    }

}
