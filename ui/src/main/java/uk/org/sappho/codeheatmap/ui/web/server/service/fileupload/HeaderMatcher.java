package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

public class HeaderMatcher {

    private final Database database;
    private List<HeaderMatch> headerMatches;

    @Inject
    public HeaderMatcher(Database database) {
        this.database = database;
    }

    public void match() {
        headerMatches = new ArrayList<HeaderMatch>();
        if (bothListsEmpty()) {
            return;
        }
        processLastImportedWithoutMatches();
        processInternalsWithoutMatches();
        processMatches();
        Collections.sort(headerMatches);
    }

    private boolean bothListsEmpty() {
        return database.getInternalHeaderNames().isEmpty() && database.getLastImportHeaderNames().isEmpty();
    }

    private void processLastImportedWithoutMatches() {
        Set<String> matches = inFirstButNotInSecond(database.getLastImportHeaderNames(),
                database.getInternalHeaderNames());
        for (String headerName : matches) {
            headerMatches.add(new HeaderMatch(headerName, null));
        }
    }

    private void processInternalsWithoutMatches() {
        Set<String> matches = inFirstButNotInSecond(database.getInternalHeaderNames(),
                database.getLastImportHeaderNames());
        for (String headerName : matches) {
            headerMatches.add(new HeaderMatch(null, headerName));
        }
    }

    private void processMatches() {
        Set<String> matches = intersect(database.getInternalHeaderNames(), database.getLastImportHeaderNames());
        for (String headerName : matches) {
            headerMatches.add(new HeaderMatch(headerName, headerName, true));
        }
    }

    private Set<String> inFirstButNotInSecond(Set<String> large, Set<String> small) {
        Set<String> matches = new HashSet<String>(large);
        matches.removeAll(small);
        return matches;
    }

    private Set<String> intersect(Set<String> set1, Set<String> set2) {
        Set<String> matches = new HashSet<String>(set1);
        matches.retainAll(set2);
        return matches;
    }

    public List<HeaderMatch> getHeaderMatches() {
        return headerMatches;
    }

}
