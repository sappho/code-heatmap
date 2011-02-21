package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.view;

import java.util.ArrayList;
import java.util.Collection;

import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class BrowseView extends ViewImpl implements BrowsePresenter.MyView {

    private ListDataProvider<String> listDataProvider;
    private final TextBox searchTerm;
    private final VerticalPanel container;

    @Inject
    public BrowseView(CodeHeatmapResources resources) {

        container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());

        HorizontalPanel searchContainer = new HorizontalPanel();
        searchContainer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        searchContainer.setSpacing(3);
        Label searchLabel = new Label("Search");
        searchTerm = new TextBox();
        searchContainer.add(searchLabel);
        searchContainer.add(searchTerm);

        CellTable<String> table = createCellTable();
        table.addStyleName(resources.css().browseTable());
        table.setWidth("960px");

        SimplePager simplePager = new SimplePager(TextLocation.LEFT);
        simplePager.setDisplay(table);

        container.add(searchContainer);
        container.add(simplePager);
        container.add(table);
    }

    private CellTable<String> createCellTable() {
        CellTable<String> table = new CellTable<String>();
        table.setPageSize(20);
        TextColumn<String> eg = new TextColumn<String>() {
            @Override
            public String getValue(String item) {
                return item;
            }
        };
        table.addColumn(eg, "E.g.");
        listDataProvider = new ListDataProvider<String>();
        listDataProvider.addDataDisplay(table);
        return table;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void setData(Collection<String> data) {
        listDataProvider.setList(new ArrayList<String>(data));
    }

    @Override
    public void addSearchTermChangeHandler(SearchCriteriaChangeEvent.Handler handler) {
        final SearchTimer searchTimer = new SearchTimer(searchTerm, handler);
        searchTerm.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                searchTimer.notifyChanged();
            }
        });
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == BrowsePresenter.TYPE_SetBrowseContent) {
            container.clear();
            container.add(content);
        } else {
            super.setInSlot(slot, content);
        }
    }
}
