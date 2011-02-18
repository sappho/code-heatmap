package uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class ChurnAnalysisResult implements Result {

    private List<Integer> filenameChangeCount;
    private String worstOffenders;

    public ChurnAnalysisResult() {
    }

    public ChurnAnalysisResult(List<Integer> filenameChangeCount, String worstOffenders) {
        this.filenameChangeCount = filenameChangeCount;
        this.worstOffenders = worstOffenders;
    }

    public List<Integer> getFilenameChangeCount() {
        return filenameChangeCount;
    }

    public String getWorstOffenders() {
        return worstOffenders;
    }

}
