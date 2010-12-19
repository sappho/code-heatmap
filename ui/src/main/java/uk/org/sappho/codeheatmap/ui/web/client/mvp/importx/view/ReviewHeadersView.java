package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import uk.org.sappho.codeheatmap.ui.web.client.events.SelectAllEvent;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

public class ReviewHeadersView extends Composite {

    private final HeaderMatchingTable headerMatchingTable;
    private ListDataProvider<HeaderMatch> headerMatches;

    public ReviewHeadersView(EventBus eventBus, CodeHeatmapResources resources) {

        VerticalPanel container = new VerticalPanel();
        initWidget(container);

        HorizontalPanel headerComparisonContainer = new HorizontalPanel();
        container.add(headerComparisonContainer);

        headerMatchingTable = new HeaderMatchingTable(eventBus, resources);
        eventBus.addHandler(SelectAllEvent.TYPE, new SelectAllEvent.Handler() {
            @Override
            public void onSelectAll(SelectAllEvent selectAllEvent) {
                List<HeaderMatch> data = headerMatches.getList();
                for (HeaderMatch headerMatch : data) {
                    headerMatch.setSelected(selectAllEvent.isSelected());
                }
                headerMatches.refresh();
            }
        });

        SimplePager pager = new SimplePager(TextLocation.LEFT);
        pager.setDisplay(headerMatchingTable.getDataDisplay());

        container.add(pager);
        container.add(headerMatchingTable);
    }

    public void setHeaderNameMatches(List<HeaderMatch> headerNameMatches) {
        headerMatches = new ListDataProvider<HeaderMatch>(headerNameMatches);
        headerMatches.addDataDisplay(headerMatchingTable.getDataDisplay());
    }

    public List<HeaderMatch> getHeaderNameMatches() {
        return headerMatches.getList();
    }
}
