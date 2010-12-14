package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.view;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.AreaChart.Options;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CumulativeFlowView extends Composite implements CumulativeFlowPresenter.Display {

    private final VerticalPanel container;
    private List<DataItem> data;

    @Inject
    public CumulativeFlowView(CodeHeatmapResources resources) {
        container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());
        initWidget(container);
    }

    @Override
    public void run() {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Release");
        dataTable.addColumn(ColumnType.NUMBER, "Total changes");
        dataTable.addColumn(ColumnType.NUMBER, "Total defects");
        dataTable.addRows(data.size());
        int row = 0;
        for (DataItem dataItem : data) {
            dataTable.setValue(row, 0, dataItem.getRelease());
            dataTable.setValue(row, 1, dataItem.getChanges());
            dataTable.setValue(row, 2, dataItem.getDefects());
            row++;
        }

        Options options = AreaChart.Options.create();
        options.setWidth(800);
        options.setHeight(600);
        options.setStacked(true);

        AreaChart chart = new AreaChart(dataTable, options);
        container.add(chart);

    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void setData(List<DataItem> data) {
        this.data = data;
    }
}
