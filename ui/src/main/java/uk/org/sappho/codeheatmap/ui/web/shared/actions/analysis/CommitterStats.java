package uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis;

import java.io.Serializable;

public class CommitterStats implements Serializable {

    private static final long serialVersionUID = -7869028614898536166L;

    private String committer;
    private int filesCreatedOrChanged;
    private int defectsCaused;
    private String responsibilities;
    private double ratio;

    public CommitterStats() {
    }

    public CommitterStats(String committer, Integer filesCreatedOrChanged, Integer defectsCaused,
            String responsibilities) {
        this.committer = committer;
        this.filesCreatedOrChanged = filesCreatedOrChanged;
        this.defectsCaused = defectsCaused;
        this.responsibilities = responsibilities;
        this.ratio = Double.valueOf(defectsCaused) / filesCreatedOrChanged;
    }

    public String getCommitter() {
        return committer;
    }

    public int getFilesCreatedOrChanged() {
        return filesCreatedOrChanged;
    }

    public int getDefectsCaused() {
        return defectsCaused;
    }

    public double getRatio() {
        return ratio;
    }

    public double getRatioNegated() {
        return 0 - ratio;
    }

    public String getResponsibilities() {
        return responsibilities;
    }
}
