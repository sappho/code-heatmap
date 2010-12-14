package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse;

import java.util.Collection;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

import com.google.inject.Inject;

public class BrowsePresenter extends WidgetPresenter<BrowsePresenter.Display> {

    private final DispatchAsync dispatch;

    public interface Display extends WidgetDisplay {
        void setData(Collection<Party> parties);

        void addSearchTermChangeHandler(SearchCriteriaChangeEvent.Handler searchCriteriaChangeHandler);
    }

    @Inject
    public BrowsePresenter(Display display, EventBus eventBus, DispatchAsync dispatch) {
        super(display, eventBus);
        this.dispatch = dispatch;
    }

    @Override
    protected void onRevealDisplay() {
        runSearch("");
    }

    private void runSearch(String searchTerm) {
    }

    @Override
    protected void onBind() {
        display.addSearchTermChangeHandler(new SearchChangeHandler());
    }

    @Override
    protected void onUnbind() {

    }

    private final class SearchChangeHandler implements SearchCriteriaChangeEvent.Handler {
        @Override
        public void onSearchCriteriaChanged(SearchCriteriaChangeEvent event) {
            runSearch(event.getSearchTerm());
        }
    }

}