package uk.org.sappho.codeheatmap.ui.web.client.mvp.main;

import uk.org.sappho.codeheatmap.ui.web.client.mvp.main.view.Menu;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class MainPresenter extends Presenter<MainPresenter.MyView, MainPresenter.MyProxy> {

    public static final String nameToken = "main";
    private final Menu subMenu;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

    public interface MyView extends View {
    }

    @ProxyStandard
    @NameToken(nameToken)
    public interface MyProxy extends Proxy<MainPresenter>, Place {

    }

    @Inject
    public MainPresenter(EventBus eventBus, MyView view, MyProxy proxy, Menu subMenu) {
        super(eventBus, view, proxy);
        this.subMenu = subMenu;
    }

    @Override
    protected void onReset() {
        super.onReset();
        subMenu.clear();
    }

    @Override
    protected void revealInParent() {
        RevealRootLayoutContentEvent.fire(this, this);
    }

}