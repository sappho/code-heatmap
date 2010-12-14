package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import java.util.List;
import java.util.Set;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

public interface FileParser {

    void parse(String input);

    public List<Party> getParties();

    public Set<String> getHeaderNames();
}
