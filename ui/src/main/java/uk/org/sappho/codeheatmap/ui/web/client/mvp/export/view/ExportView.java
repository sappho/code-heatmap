package uk.org.sappho.codeheatmap.ui.web.client.mvp.export.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.export.ExportPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

public class ExportView extends Composite implements ExportPresenter.Display {

    private final LayoutPanel containerPanel;

    @Inject
    public ExportView(CodeHeatmapResources resources) {

        containerPanel = new LayoutPanel();
        containerPanel.setWidth("960px");
        containerPanel.addStyleName(resources.css().centerLayout());
        initWidget(containerPanel);

    }

    @Override
    public Widget asWidget() {
        return this;
    }

}
