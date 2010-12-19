package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

import uk.org.sappho.codeheatmap.ui.web.client.events.SelectAllEvent;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

public class HeaderMatchingTable extends Composite {

    private CellTable<HeaderMatch> table;
    private final EventBus eventBus;

    public HeaderMatchingTable(EventBus eventBus, final CodeHeatmapResources resources) {

        this.eventBus = eventBus;
        table = new CellTable<HeaderMatch>();
        table.setWidth("100%");
        table.addStyleName(resources.css().headerMatchTable());
        table.setSelectionModel(new MultiSelectionModel<HeaderMatch>());
        table.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);

        table.setRowStyles(new RowStyles<HeaderMatch>() {
            @Override
            public String getStyleNames(HeaderMatch row, int rowIndex) {
                if (row.getLastImport() != null && row.getInternal() != null) {
                    return resources.css().fullMatch();
                }
                return "";
            }
        });
        table.addColumn(new TextColumn<HeaderMatch>() {
            @Override
            public String getValue(HeaderMatch headerMatch) {
                return headerMatch.getLastImport();
            }
        }, "Imported");
        table.addColumn(new TextColumn<HeaderMatch>() {
            @Override
            public String getValue(HeaderMatch headerMatch) {
                return headerMatch.getInternal();
            }
        }, "Internal");

        Column<HeaderMatch, Boolean> selectForImportColumn =
                new Column<HeaderMatch, Boolean>(new CheckboxCell(true)) {
                    @Override
                    public Boolean getValue(HeaderMatch headerMatch) {
                        return headerMatch.isSelected();
                    }
                };
        selectForImportColumn.setFieldUpdater(new FieldUpdater<HeaderMatch, Boolean>() {
            @Override
            public void update(int index, HeaderMatch headerMatch, Boolean value) {
                headerMatch.setSelected(value);
            }
        });

        CheckboxCellHeader selectForImportHeader = new CheckboxCellHeader();
        table.addColumn(selectForImportColumn, selectForImportHeader);

        initWidget(table);
    }

    public HasData<HeaderMatch> getDataDisplay() {
        return table;
    }

    private final class CheckboxCellHeader extends Header<Boolean> {

        private CheckboxCellHeader() {
            super(new CheckboxCell());
            setUpdater(new ValueUpdater<Boolean>() {
                @Override
                public void update(Boolean value) {
                    eventBus.fireEvent(new SelectAllEvent(value));
                }
            });
        }

        @Override
        public Boolean getValue() {
            return false;
        }
    }
}
