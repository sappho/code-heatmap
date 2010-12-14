package uk.org.sappho.codeheatmap.ui.web.shared.actions;

import java.io.Serializable;

public class DataItem implements Serializable {

    private static final long serialVersionUID = -9192883656342874747L;

    private String release;
    private int changes;
    private int defects;

    public DataItem() {
    }

    public DataItem(String release, int changes, int defects) {
        this.release = release;
        this.changes = changes;
        this.defects = defects;
    }

    public String getRelease() {
        return release;
    }

    public int getChanges() {
        return changes;
    }

    public int getDefects() {
        return defects;
    }

}
