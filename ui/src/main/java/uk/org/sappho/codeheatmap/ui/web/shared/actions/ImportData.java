package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

import net.customware.gwt.dispatch.shared.Action;

public class ImportData implements Action<ImportDataResult> {

    private List<HeaderMatch> headerNameMatches;

    public ImportData() {
    }

    public ImportData(List<HeaderMatch> headerNameMatches) {
        this.headerNameMatches = headerNameMatches;
    }

    public List<HeaderMatch> getHeaderNameMatches() {
        return headerNameMatches;
    }
}
