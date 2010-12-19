package uk.org.sappho.codeheatmap.ui.web.shared.model;

import java.io.Serializable;

public class PartyPropertyDifference implements Serializable {

    private static final long serialVersionUID = -4154187973536189730L;

    private String header;
    private String newValue;
    private String internalValue;

    public PartyPropertyDifference() {
    }

    public PartyPropertyDifference(String header, String newValue, String internalValue) {
        this.header = header;
        this.newValue = newValue;
        this.internalValue = internalValue;
    }

    public String getHeader() {
        return header;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getInternalValue() {
        return internalValue;
    }

}
