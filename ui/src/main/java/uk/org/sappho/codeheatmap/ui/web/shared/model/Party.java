package uk.org.sappho.codeheatmap.ui.web.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Party implements Serializable {

    private static final long serialVersionUID = 6032286460328531520L;

    private String id;
    private Map<String, String> properties = new HashMap<String, String>();
    private boolean isNew;
    private List<PartyPropertyDifference> propertyDifferences = new ArrayList<PartyPropertyDifference>();

    public Party() {
    }

    public Party(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperty(String header, String value) {
        properties.put(header, value);
    }

    public String getProperty(String header) {
        return properties.get(header);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public List<PartyPropertyDifference> getPropertyDifferences() {
        return propertyDifferences;
    }

    public Party withDifferentProperty(String header, String internalValue) {
        propertyDifferences.add(new PartyPropertyDifference(header, getProperty(header), internalValue));
        return this;
    }

    public boolean hasDifferentHeaders() {
        return !propertyDifferences.isEmpty();
    }

    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

}
