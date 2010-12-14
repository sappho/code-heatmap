package uk.org.sappho.codeheatmap.ui.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class DispatchCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        if (caught instanceof StatusCodeException) {
            GWT.log("dispatch fail - HTTP " + ((StatusCodeException) caught).getStatusCode(), caught);
        } else {
            throw new RuntimeException(caught);
        }
    }

}
