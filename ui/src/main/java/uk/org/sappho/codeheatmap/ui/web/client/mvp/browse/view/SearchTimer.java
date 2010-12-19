package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.view;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;

import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;

public class SearchTimer {

    private Timer timer;

    public SearchTimer(final TextBox searchTerm, final SearchCriteriaChangeEvent.Handler handler) {
        timer = new Timer() {
            @Override
            public void run() {
                handler.onSearchCriteriaChanged(new SearchCriteriaChangeEvent(searchTerm.getValue()));
            }
        };
    }

    public void notifyChanged() {
        timer.schedule(500);
    }
}
