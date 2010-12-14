package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.util.List;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

import net.customware.gwt.dispatch.shared.Result;

public class FetchDataDifferencesResult implements Result {

    private List<Party> dataDifferences;

    public FetchDataDifferencesResult() {
    }

    public FetchDataDifferencesResult(List<Party> dataDifferences) {
        this.dataDifferences = dataDifferences;
    }

    public List<Party> getDataDifferences() {
        return dataDifferences;
    }

}
