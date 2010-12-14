package uk.org.sappho.codeheatmap.ui.web.client.place;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Provider;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.export.ExportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.MainPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.place.ExportPlace;
import uk.org.sappho.codeheatmap.ui.web.client.place.MainPlace;

@RunWith(MockitoJUnitRunner.class)
public class ExportPlaceTest {

    @Mock
    private Provider<ExportPresenter> mockPresenterProvider;
    @Mock
    private ExportPresenter mockPresenter;
    @Mock
    private Provider<MainPlace> mockMainPlaceProvider;
    @Mock
    private ExportPresenter.Display mockDisplay;
    @Mock
    private MainPlace mockMainPlace;
    @Mock
    private MainPresenter mockMainPresenter;

    @Test
    public void shouldTellLayoutToDisplayTheExportWidget() {

        when(mockMainPlaceProvider.get()).thenReturn(mockMainPlace);
        when(mockMainPlace.getPresenter()).thenReturn(mockMainPresenter);
        when(mockPresenter.getDisplay()).thenReturn(mockDisplay);
        ExportPlace exportPlace = new ExportPlace(mockPresenterProvider, mockMainPlaceProvider);

        exportPlace.preparePresenter(null, mockPresenter);

        verify(mockMainPresenter).setContent(any(Widget.class));

    }
}
