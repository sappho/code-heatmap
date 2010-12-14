package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.model.DataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class ReviewDataView extends Composite {

    private CellTable<DataInfo> dataInfoTable;
    private final CodeHeatmapResources resources;
    private SimplePanel pagerContainer;
    private SimplePanel diffTableContainer;

    public ReviewDataView(CodeHeatmapResources resources) {
        this.resources = resources;
        VerticalPanel container = new VerticalPanel();
        initWidget(container);

        createDataInfoTable();

        container.add(dataInfoTable);
        pagerContainer = new SimplePanel();
        diffTableContainer = new SimplePanel();
        container.add(pagerContainer);
        container.add(diffTableContainer);
    }

    private void createDataInfoTable() {
        dataInfoTable = new CellTable<DataInfo>();
        dataInfoTable.addStyleName(resources.css().dataInfoTable());

        dataInfoTable.addColumn(new TextColumn<DataInfo>() {
            @Override
            public String getValue(DataInfo dataInfo) {
                return dataInfo.getKey();
            }
        });
        dataInfoTable.addColumn(new TextColumn<DataInfo>() {
            @Override
            public String getValue(DataInfo dataInfo) {
                return dataInfo.getValue();
            }
        });
    }

    private CellTable<Party> createDataDifferencesTables() {

        CellTable<Party> dataDifferencesTable = new CellTable<Party>();
        dataDifferencesTable.setPageSize(10);
        dataDifferencesTable.addStyleName(resources.css().dataDifferencesTable());
        dataDifferencesTable.setRowStyles(new RowStyles<Party>() {
            @Override
            public String getStyleNames(Party party, int rowIndex) {
                if (party.isNew()) {
                    return resources.css().newParty();
                }
                if (party.hasDifferentHeaders()) {
                    return resources.css().differentParty();
                }
                return "";
            }
        });

        return dataDifferencesTable;

    }

    public void setDataInfo(List<DataInfo> dataInfo) {
        ListDataProvider<DataInfo> dataInfoProvider = new ListDataProvider<DataInfo>(dataInfo);
        dataInfoProvider.addDataDisplay(dataInfoTable);
    }

    public void setDataDifferences(List<HeaderMatch> selectedHeaders, List<Party> dataDifferences) {

        if (!dataDifferences.isEmpty()) {
            CellTable<Party> dataDifferencesTable = createDataDifferencesTables();
            for (final HeaderMatch headerMatch : selectedHeaders) {
                dataDifferencesTable.addColumn(new TextColumn<Party>() {
                    @Override
                    public String getValue(Party party) {
                        return party.getProperty(headerMatch.getLastImport());
                    }
                }, headerMatch.getLastImport());
            }

            ListDataProvider<Party> dataDifferencesProvider = new ListDataProvider<Party>(dataDifferences);
            dataDifferencesProvider.addDataDisplay(dataDifferencesTable);

            SimplePager pager = new SimplePager(TextLocation.LEFT);
            pager.setDisplay(dataDifferencesTable);

            pagerContainer.setWidget(pager);
            diffTableContainer.setWidget(dataDifferencesTable);
        } else {
            pagerContainer.clear();
            diffTableContainer.clear();
        }
    }
}
