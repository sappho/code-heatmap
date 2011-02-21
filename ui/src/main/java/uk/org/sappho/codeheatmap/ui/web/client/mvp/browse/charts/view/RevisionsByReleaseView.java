package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.RevisionsByReleasePresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ReleaseChangesDefects;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.AreaChart.Options;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.ViewImpl;

@Singleton
public class RevisionsByReleaseView extends ViewImpl implements RevisionsByReleasePresenter.MyView {

    private final VerticalPanel container;
    private List<ReleaseChangesDefects> data;

    @Inject
    public RevisionsByReleaseView(CodeHeatmapResources resources) {
        container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());
    }

    @Override
    public void run() {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Release");
        dataTable.addColumn(ColumnType.NUMBER, "Change revisions");
        dataTable.addColumn(ColumnType.NUMBER, "Defect revisions");
        dataTable.addRows(data.size());
        int row = 0;
        for (ReleaseChangesDefects dataItem : data) {
            dataTable.setValue(row, 0, dataItem.getRelease());
            dataTable.setValue(row, 1, dataItem.getChanges());
            dataTable.setValue(row, 2, dataItem.getDefects());
            row++;
        }

        Options options = AreaChart.Options.create();
        options.setWidth(1024);
        options.setHeight(600);
        options.setStacked(false);
        options.setLegend(LegendPosition.BOTTOM);

        AreaChart chart = new AreaChart(dataTable, options);

        container.add(new Label("Revisions by Release"));
        container.add(chart);

    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void setData(List<ReleaseChangesDefects> data) {
        this.data = data;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
