package uk.org.sappho.codeheatmap.ui.web.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface CodeHeatmapResources extends ClientBundle {

    @CssResource.NotStrict
    @Source("CodeHeatmap.css")
    CodeHeatmapCss css();

    @Source("pleaseWait.gif")
    ImageResource pleaseWait();

}
