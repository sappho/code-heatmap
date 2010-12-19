package uk.org.sappho.codeheatmap.ui.web.shared.model;

import java.io.Serializable;

public class HeaderMatch implements Serializable, Comparable<HeaderMatch> {

    private static final long serialVersionUID = -7875335686811293752L;

    private String internal;
    private String lastImport;
    private Boolean selected;

    public HeaderMatch() {
    }

    public HeaderMatch(String lastImport, String internal) {
        this(lastImport, internal, false);
    }

    public HeaderMatch(String lastImport, String internal, boolean selected) {
        this.lastImport = lastImport;
        this.internal = internal;
        this.selected = selected;
    }

    public String getLastImport() {
        return lastImport;
    }

    public String getInternal() {
        return internal;
    }

    public Boolean isSelected() {
        return selected;
    }

    @Override
    public int compareTo(HeaderMatch o) {
        if (o == null) {
            return -1;
        }
        return (internal + lastImport).compareTo(o.getInternal() + o.getLastImport());
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
