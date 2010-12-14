package uk.org.sappho.codeheatmap.ui.web.shared.model;

import com.google.gwt.view.client.ProvidesKey;

public class PartyKeyProvider implements ProvidesKey<Party> {

    @Override
    public String getKey(Party party) {
        return party.getProperty("Accountid")
                + party.getProperty("Duns")
                + party.getProperty("PMLedgerName");
    }
}
