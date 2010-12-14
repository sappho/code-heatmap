package uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.view;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.browse.cf.CumulativeFlowPresenter;
import uk.org.sappho.codeheatmap.ui.web.client.resources.CodeHeatmapResources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class CumulativeFlowView extends Composite implements CumulativeFlowPresenter.Display {

    @Inject
    public CumulativeFlowView(CodeHeatmapResources resources) {
        Label label = new Label("cf");
        initWidget(label);
    }
}
