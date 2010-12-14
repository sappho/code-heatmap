package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.view;

import java.util.ArrayList;
import java.util.Collection;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.events.SearchCriteriaChangeEvent;
import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.BrowsePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class BrowseView extends Composite implements BrowsePresenter.Display {

    private ListDataProvider<Party> listDataProvider;
    private final TextBox searchTerm;

    @Inject
    public BrowseView(CodeHeatmapResources resources, EventBus eventBus) {

        VerticalPanel container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());
        initWidget(container);

        HorizontalPanel searchContainer = new HorizontalPanel();
        searchContainer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        searchContainer.setSpacing(3);
        Label searchLabel = new Label("Search");
        searchTerm = new TextBox();
        searchContainer.add(searchLabel);
        searchContainer.add(searchTerm);

        CellTable<Party> table = createCellTable();
        table.addStyleName(resources.css().browseTable());
        table.setWidth("960px");

        SimplePager simplePager = new SimplePager(TextLocation.LEFT);
        simplePager.setDisplay(table);

        container.add(searchContainer);
        container.add(simplePager);
        container.add(table);
    }

    private CellTable<Party> createCellTable() {
        CellTable<Party> table = new CellTable<Party>();
        table.setPageSize(20);
        TextColumn<Party> duns = new TextColumn<Party>() {
            @Override
            public String getValue(Party party) {
                return party.getProperty("Duns");
            }
        };
        TextColumn<Party> businessName = new TextColumn<Party>() {
            @Override
            public String getValue(Party party) {
                return party.getProperty("BusinessName");
            }
        };
        TextColumn<Party> dbDelinquency = new TextColumn<Party>() {
            @Override
            public String getValue(Party party) {
                return party.getProperty("DBDelinquency");
            }
        };
        TextColumn<Party> dbFailureScore1 = new TextColumn<Party>() {
            @Override
            public String getValue(Party party) {
                return party.getProperty("DBFailureScore1");
            }
        };
        TextColumn<Party> ledgerCustom2 = new TextColumn<Party>() {
            @Override
            public String getValue(Party party) {
                return party.getProperty("LedgerCustom2");
            }
        };
        table.addColumn(duns, "AAA");
        table.addColumn(businessName, "BBB");
        table.addColumn(ledgerCustom2, "CCC");
        table.addColumn(dbDelinquency, "DDD");
        table.addColumn(dbFailureScore1, "EEE");
        listDataProvider = new ListDataProvider<Party>();
        listDataProvider.addDataDisplay(table);
        return table;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setData(Collection<Party> parties) {
        listDataProvider.setList(new ArrayList<Party>(parties));
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
}
