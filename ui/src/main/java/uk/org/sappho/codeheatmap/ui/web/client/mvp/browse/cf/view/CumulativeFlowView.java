package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.view;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

public class CumulativeFlowView extends Composite implements CumulativeFlowPresenter.Display {

    @Inject
    public CumulativeFlowView(CodeHeatmapResources resources) {
        VerticalPanel container = new VerticalPanel();
        container.addStyleName(resources.css().centerLayout());
        initWidget(container);

        container.add(new Label("cf"));
    }
}
