package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class AnalyseDifferences {

    private final Database database;
    private final List<Party> differences = new ArrayList<Party>();

    @Inject
    public AnalyseDifferences(Database database) {
        this.database = database;
    }

    public void analyse() {

        differences.clear();

        Map<String, Party> lastImportParties = database.getLastImportParties();
        Map<String, Party> internalParties = database.getInternalParties();
        for (String partyId : lastImportParties.keySet()) {
            processParty(lastImportParties, internalParties, partyId);
        }
    }

    private void processParty(Map<String, Party> lastImportParties, Map<String, Party> internalParties,
            String newPartyId) {
        if (knowAboutThisParty(internalParties, newPartyId)) {
            lookForDifferences(lastImportParties, internalParties, newPartyId);
        } else {
            processNewParty(lastImportParties, newPartyId);
        }
    }

    private void lookForDifferences(Map<String, Party> lastImportParties, Map<String, Party> internalParties,
            String partyId) {
        Party party1 = lastImportParties.get(partyId);
        Party party2 = internalParties.get(partyId);
        for (String header : database.getInternalHeaderNames()) {
            if (party1.getProperty(header) == null && party2.getProperty(header) == null) {
                return;
            }
            if (party1.getProperty(header) == null && party2.getProperty(header) != null) {
                differences.add(party1.withDifferentProperty(header, party2.getProperty(header)));
            }
            if (!party1.getProperty(header).equals(party2.getProperty(header))) {
                differences.add(party1.withDifferentProperty(header, party2.getProperty(header)));
            }
        }
    }

    private void processNewParty(Map<String, Party> lastImportParties, String newPartyId) {
        Party newParty = lastImportParties.get(newPartyId);
        newParty.setNew(true);
        differences.add(newParty);
    }

    private boolean knowAboutThisParty(Map<String, Party> internalParties, String partyId) {
        return internalParties.containsKey(partyId);
    }

    public List<Party> getDifferences() {
        return differences;
    }

}
