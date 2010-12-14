package uk.org.sappho.codeheatmap.ui.web.shared.model;

import java.io.Serializable;

public class DataInfo implements Serializable {

    private static final long serialVersionUID = -6646628443917975369L;

    private String key;
    private String value;

    public DataInfo() {
    }

    public DataInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}