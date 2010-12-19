package uk.org.sappho.codeheatmap.ui.web.client.mvp.main;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainPresenter extends WidgetPresenter<MainPresenter.Display> {

    public interface Display extends WidgetDisplay {

        void setContent(Widget content);
    }

    @Inject
    public MainPresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
    }

    @Override
    protected void onRevealDisplay() {
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onUnbind() {
    }

    public void setContent(Widget content) {
        display.setContent(content);
    }

}