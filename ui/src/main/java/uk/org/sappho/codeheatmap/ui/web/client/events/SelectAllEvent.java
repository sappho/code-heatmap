package uk.org.sappho.codeheatmap.ui.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SelectAllEvent extends GwtEvent<SelectAllEvent.Handler> {

    public static final Type<SelectAllEvent.Handler> TYPE = new Type<SelectAllEvent.Handler>();
    private final boolean selected;

    public SelectAllEvent(boolean selected) {
        this.selected = selected;
    }

    @Override
    public GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectAllEvent.Handler handler) {
        handler.onSelectAll(this);
    }

    public boolean isSelected() {
        return selected;
    }

    public interface Handler extends EventHandler {
        void onSelectAll(SelectAllEvent selectAllEvent);
    }

}
