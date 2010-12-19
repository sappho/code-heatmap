package uk.org.sappho.codeheatmap.ui.web.server.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public class Database {

    public enum Sort {
        ASC,
        DESC
    }

    private final Map<String, Party> lastImportParties = new HashMap<String, Party>();
    private final Map<String, Party> internalParties = new HashMap<String, Party>();
    private Set<String> lastImportHeaderNames = new HashSet<String>();
    private Set<String> internalHeaderNames = new HashSet<String>();
    private final LuceneWrapper luceneWrapper;

    @Inject
    public Database(LuceneWrapper luceneWrapper) {
        this.luceneWrapper = luceneWrapper;
    }

    public Map<String, Party> getLastImportParties() {
        return lastImportParties;
    }

    public void saveLastImportParties(List<Party> newLastImportParties) {
        lastImportParties.clear();
        for (Party party : newLastImportParties) {
            if (lastImportParties.containsKey(party.getId())) {
                throw new RuntimeException("Found non-unique key: " + party.getId());
            }
            lastImportParties.put(party.getId(), party);
        }
    }

    public Map<String, Party> getInternalParties() {
        return internalParties;
    }

    public List<Party> getSortedInternalParties(String sortHeader, Sort sortDirection) {
        List<Party> sortedParties = new ArrayList<Party>(internalParties.values());
        if (sortDirection == Sort.ASC) {
            Collections.sort(sortedParties, new PartyComparator(sortHeader));
        } else {
            Collections.sort(sortedParties, Ordering.from(new PartyComparator(sortHeader)).reverse());
        }
        return new ArrayList<Party>(sortedParties);
    }

    public void setInternalParties(List<Party> newParties) {
        internalParties.clear();
        for (Party party : newParties) {
            if (internalParties.containsKey(party.getId())) {
                throw new RuntimeException("Found non-unique key: " + party.getId());
            }
            this.internalParties.put(party.getId(), party);
            luceneWrapper.save(party);
        }
    }

    public void saveLastImportHeaderNames(Set<String> lastImportHeaderNames) {
        this.lastImportHeaderNames = lastImportHeaderNames;
    }

    public Set<String> getLastImportHeaderNames() {
        return lastImportHeaderNames;
    }

    public void saveInternalHeaderNames(Set<String> internalHeaderNames) {
        this.internalHeaderNames = internalHeaderNames;
    }

    public Set<String> getInternalHeaderNames() {
        return internalHeaderNames;
    }

    public List<Party> findPartiesByTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.equals("")) {
            return getSortedInternalParties("BusinessName", Sort.ASC);
        }

        List<String> partyIds = luceneWrapper.findPartyIdsByTerm(searchTerm);
        List<Party> results = new ArrayList<Party>();
        for (String partyId : partyIds) {
            results.add(internalParties.get(partyId));
        }
        return results;
    }
}
