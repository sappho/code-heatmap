package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.ImportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.NavigationHandler;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.model.DataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

@Singleton
public class ImportView extends Composite implements ImportPresenter.Display {

    private final DeckPanel deck;
    private final Button nextButton;
    private final Button finishButton;
    private Page currentPage;
    private final ReviewHeadersView reviewHeadersView;
    private final ReviewDataView reviewDataView;
    private final Label title;

    @Inject
    public ImportView(CodeHeatmapResources resources, EventBus eventBus) {

        VerticalPanel containerPanel = new VerticalPanel();
        containerPanel.setWidth("700px");
        containerPanel.addStyleName(resources.css().centerLayout());
        containerPanel.setSpacing(20);
        initWidget(containerPanel);

        deck = new DeckPanel();
        deck.addStyleName(resources.css().thickBorder());

        reviewHeadersView = new ReviewHeadersView(eventBus, resources);
        reviewDataView = new ReviewDataView(resources);

        deck.insert(new UploadFileView(), Page.UPLOAD_FILE.ordinal());
        deck.insert(reviewHeadersView, Page.REVIEW_HEADERS.ordinal());
        deck.insert(reviewDataView, Page.REVIEW_DATA.ordinal());
        deck.insert(new ConfirmImportView(), Page.CONFIRM_IMPORT.ordinal());

        HorizontalPanel buttonRow = new HorizontalPanel();
        nextButton = new Button("Next");
        finishButton = new Button("Finish");
        buttonRow.add(nextButton);
        buttonRow.add(finishButton);

        title = new Label();
        title.addStyleName(resources.css().wizardPageTitle());

        containerPanel.add(title);
        containerPanel.add(deck);
        containerPanel.add(buttonRow);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void gotoPage(Page page) {
        deck.showWidget(page.ordinal());
        currentPage = page;
        nextButton.setEnabled(page.ordinal() < Page.values().length - 1);
        finishButton.setEnabled(page.ordinal() == Page.values().length - 1);
    }

    @Override
    public void bindToNextButton(final NavigationHandler navigationHandler) {
        nextButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                navigationHandler.gotoPageAfter(currentPage);
            }
        });
    }

    @Override
    public void bindToFinishButton(final NavigationHandler navigationHandler) {
        finishButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                navigationHandler.finish();
            }
        });
    }

    @Override
    public void setHeaderNameMatches(List<HeaderMatch> headerNameMatches) {
        reviewHeadersView.setHeaderNameMatches(headerNameMatches);
    }

    @Override
    public List<HeaderMatch> getHeaderNameMatches() {
        return reviewHeadersView.getHeaderNameMatches();
    }

    @Override
    public void setDataInfo(List<DataInfo> dataInfo) {
        reviewDataView.setDataInfo(dataInfo);
    }

    @Override
    public void setDataDifferences(List<Party> dataDifferences) {
        reviewDataView.setDataDifferences(getSelectedHeaders(), dataDifferences);
    }

    private List<HeaderMatch> getSelectedHeaders() {
        List<HeaderMatch> allHeaders = reviewHeadersView.getHeaderNameMatches();
        List<HeaderMatch> selectedHeaders = new ArrayList<HeaderMatch>();
        for (HeaderMatch headerMatch : allHeaders) {
            if (headerMatch.isSelected()) {
                selectedHeaders.add(headerMatch);
            }
        }
        return selectedHeaders;
    }

    @Override
    public void setPageTitle(String titleString) {
        title.setText(titleString);
    }

}
