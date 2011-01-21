package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.HandleDataForIssuesByRelease;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.IssuesByReleasePresenter.Display;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;

@RunWith(MockitoJUnitRunner.class)
public class IssuesByReleasePresenterTest {

    @Mock
    private EventBus mockEventBus;
    @Mock
    private Display mockDisplay;
    @Mock
    private DispatchAsync mockDispatch;
    @Mock
    private HandleDataForIssuesByRelease mockHandleFetchedData;
    private IssuesByReleasePresenter issuesByReleasePresenter;

    @Before
    public void setup() {
        issuesByReleasePresenter = new IssuesByReleasePresenter(mockDisplay, mockEventBus, mockDispatch,
                mockHandleFetchedData);
    }

    @Test
    public void shouldFetchDataOnReveal() {
        issuesByReleasePresenter.onRevealDisplay();
        verify(mockDispatch).execute(isA(FetchData.class), isA(HandleDataForIssuesByRelease.class));
    }
}
