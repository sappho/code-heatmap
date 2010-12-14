package uk.org.sappho.codeheatmap.ui.web.server.database;

import java.util.Comparator;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class PartyComparator implements Comparator<Party> {

    private final String sortHeader;

    public PartyComparator(String sortHeader) {
        this.sortHeader = sortHeader;
    }

    @Override
    public int compare(Party o1, Party o2) {
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return +1;
        }
        String party1FieldValue = o1.getProperty(sortHeader);
        String party2Fieldvalue = o2.getProperty(sortHeader);
        return party1FieldValue.compareTo(party2Fieldvalue);

    }
}
