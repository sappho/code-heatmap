package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;
import uk.org.sappho.codeheatmap.ui.web.shared.model.PartyKeyProvider;

public class TabDelimitedFileParser implements FileParser {

    private final List<Party> parties = new ArrayList<Party>();
    private final List<String> headerNames = new ArrayList<String>();
    private final PartyKeyProvider partyKeyProvider;

    @Inject
    public TabDelimitedFileParser(PartyKeyProvider partyKeyProvider) {
        this.partyKeyProvider = partyKeyProvider;
    }

    @Override
    public void parse(String input) {
        parties.clear();
        headerNames.clear();

        String[] lines = input.split("\\r\\n|\\n");
        if (lines.length < 1) {
            return;
        }
        parseHeaderLine(lines[0]);
        for (int i = 1; inDevMode(i, lines.length); i++) {
            String line = lines[i];
            String[] columns = line.split("\\t");
            Party party = new Party(null);
            for (int j = 0; j < columns.length; j++) {
                party.setProperty(headerNames.get(j), columns[j]);
            }
            party.setId(partyKeyProvider.getKey(party));
            parties.add(party);
        }
    }

    private boolean inDevMode(int i, int length) {
        //        return i < Math.min(50, length);
        return i < length;
    }

    private void parseHeaderLine(String headerLine) {
        String[] headers = headerLine.split("\\t");
        for (String header : headers) {
            headerNames.add(header);
        }
    }

    @Override
    public List<Party> getParties() {
        return parties;
    }

    @Override
    public Set<String> getHeaderNames() {
        return new HashSet<String>(headerNames);
    }

}
