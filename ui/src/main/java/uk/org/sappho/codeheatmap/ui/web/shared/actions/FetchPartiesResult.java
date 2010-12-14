package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

import net.customware.gwt.dispatch.shared.Result;

public class FetchPartiesResult implements Result {

    private static final long serialVersionUID = -1567599726349527589L;
    private List<Party> parties;

    public FetchPartiesResult() {
    }

    public FetchPartiesResult(List<Party> parties) {
        this.parties = parties;
    }

    public List<Party> getParties() {
        return parties;
    }

}
