package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.shared.model.DataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class ImportPresenter extends WidgetPresenter<ImportPresenter.Display> {

    private final NavigationHandler navigationHandler;

    public interface Display extends WidgetDisplay {
        enum Page {
            UPLOAD_FILE,
            REVIEW_HEADERS,
            REVIEW_DATA,
            CONFIRM_IMPORT
        }

        void gotoPage(Page page);

        void bindToNextButton(NavigationHandler navigationHandler);

        void bindToFinishButton(NavigationHandler navigationHandler);

        void setHeaderNameMatches(List<HeaderMatch> headerNameMatches);

        void setDataInfo(List<DataInfo> dataInfo);

        void setDataDifferences(List<Party> dataDifferences);

        void setPageTitle(String title);

        List<HeaderMatch> getHeaderNameMatches();

    }

    @Inject
    public ImportPresenter(Display display, EventBus eventBus, NavigationHandler nextButtonHandler) {
        super(display, eventBus);
        navigationHandler = nextButtonHandler;
    }

    @Override
    protected void onRevealDisplay() {
        navigationHandler.gotoFirstPage();
    }

    @Override
    protected void onBind() {
        display.bindToNextButton(navigationHandler);
        display.bindToFinishButton(navigationHandler);
    }

    @Override
    protected void onUnbind() {

    }
}
