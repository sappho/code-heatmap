package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.BadCommittersPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.CommitterStats;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
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

public class BadCommittersView extends ViewImpl implements BadCommittersPresenter.MyView {

    private ListDataProvider<CommitterStats> listDataProvider;
    private final TextBox searchTerm;
    private final VerticalPanel container;

    @Inject
    public BadCommittersView(CodeHeatmapResources resources) {

        container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());

        HorizontalPanel searchContainer = new HorizontalPanel();
        searchContainer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        searchContainer.setSpacing(3);
        Label searchLabel = new Label("Search");
        searchTerm = new TextBox();
        searchContainer.add(searchLabel);
        searchContainer.add(searchTerm);

        CellTable<CommitterStats> table = createCellTable();
        table.addStyleName(resources.css().browseTable());
        table.setWidth("960px");

        SimplePager simplePager = new SimplePager(TextLocation.LEFT);
        simplePager.setDisplay(table);

        container.add(searchContainer);
        container.add(simplePager);
        container.add(table);
    }

    private CellTable<CommitterStats> createCellTable() {
        CellTable<CommitterStats> table = new CellTable<CommitterStats>();
        table.setPageSize(20);
        TextColumn<CommitterStats> committer = new TextColumn<CommitterStats>() {
            @Override
            public String getValue(CommitterStats committerStats) {
                return committerStats.getCommitter();
            }
        };
        TextColumn<CommitterStats> newCommits = new TextColumn<CommitterStats>() {
            @Override
            public String getValue(CommitterStats committerStats) {
                return String.valueOf(committerStats.getFilesCreatedOrChanged());
            }
        };
        TextColumn<CommitterStats> defectsCaused = new TextColumn<CommitterStats>() {
            @Override
            public String getValue(CommitterStats committerStats) {
                return String.valueOf(committerStats.getDefectsCaused());
            }
        };
        TextColumn<CommitterStats> ratio = new TextColumn<CommitterStats>() {
            @Override
            public String getValue(CommitterStats committerStats) {
                return String.valueOf(committerStats.getRatio());
            }
        };
        SafeHtmlCell responsibilitiesCell = new SafeHtmlCell();
        Column<CommitterStats, SafeHtml> responsibilitiesColumn = new Column<CommitterStats, SafeHtml>(
                responsibilitiesCell) {

            @Override
            public SafeHtml getValue(CommitterStats committerStats) {
                return TEMPLATE.messageWithLink(committerStats.getResponsibilities());
            }
        };
        table.addColumn(committer, "Committer");
        table.addColumn(newCommits, "New files");
        table.addColumn(defectsCaused, "Defects caused");
        table.addColumn(ratio, "Ratio");
        table.addColumn(responsibilitiesColumn, "Files");
        listDataProvider = new ListDataProvider<CommitterStats>();
        listDataProvider.addDataDisplay(table);
        return table;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void setData(List<CommitterStats> data) {
        listDataProvider.setList(data);
    }

    private static final MyTemplate TEMPLATE = GWT.create(MyTemplate.class);

    public interface MyTemplate extends SafeHtmlTemplates {
        @Template("<span title=\"{0}\">hover here</span>")
        SafeHtml messageWithLink(String title);
    }

}
