package uk.org.sappho.codeheatmap.ui.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SearchCriteriaChangeEvent extends GwtEvent<SearchCriteriaChangeEvent.Handler> {

    public static final Type<SearchCriteriaChangeEvent.Handler> TYPE = new Type<SearchCriteriaChangeEvent.Handler>();
    private final String searchTerm;

    public SearchCriteriaChangeEvent(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchCriteriaChangeEvent.Handler handler) {
        handler.onSearchCriteriaChanged(this);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public interface Handler extends EventHandler {
        void onSearchCriteriaChanged(SearchCriteriaChangeEvent searchCriteriaChangeEvent);
    }

}
