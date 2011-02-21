package uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class BadCommittersAnalysisResult implements Result {

    private List<CommitterStats> data;

    public BadCommittersAnalysisResult() {
    }

    public BadCommittersAnalysisResult(List<CommitterStats> data) {
        this.data = data;
    }

    public List<CommitterStats> getData() {
        return data;
    }

}
