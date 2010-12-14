package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;

import net.customware.gwt.dispatch.shared.Result;

public class FetchHeaderNamesComparisonResult implements Result {

    private List<HeaderMatch> headerMatches;

    public FetchHeaderNamesComparisonResult() {
    }

    public FetchHeaderNamesComparisonResult(List<HeaderMatch> headerMatches) {
        this.headerMatches = headerMatches;
    }

    public List<HeaderMatch> getHeaderMatches() {
        return headerMatches;
    }

}
