package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.view;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.charts.ChurnDistributionPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.ViewImpl;

@Singleton
public class ChurnDistributionView extends ViewImpl implements ChurnDistributionPresenter.MyView {

    private final VerticalPanel container;
    private List<Integer> data;
    private String worstOffenders;

    @Inject
    public ChurnDistributionView(CodeHeatmapResources resources) {
        container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());
    }

    @Override
    public void run() {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.NUMBER, "Number of changes");
        dataTable.addRows(data.size());
        int row = 0;
        for (Integer count : data) {
            dataTable.setValue(row, 0, count);
            row++;
        }

        ColumnChart.Options options = ColumnChart.Options.create();
        options.setWidth(1024);
        options.setHeight(600);
        options.setStacked(false);
        options.setShowCategories(false);
        options.setLegend(LegendPosition.BOTTOM);

        ColumnChart chart = new ColumnChart(dataTable, options);

        container.add(new Label("Churn"));
        container.add(chart);
        container.add(new HTML("<p>Worst offenders: " + worstOffenders + "</p>"));

    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void setData(List<Integer> data, String worstOffenders) {
        this.data = data;
        this.worstOffenders = worstOffenders;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

}
