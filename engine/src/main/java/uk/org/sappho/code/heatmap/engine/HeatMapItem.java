package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class HeatMapItem implements Comparable<HeatMapItem> {

    private final String name;
    private final Map<String, List<Change>> jiras = new HashMap<String, List<Change>>();

    public HeatMapItem(String name) {

        this.name = name;
    }

    public void update(Change change) {

        String jiraId = change.getJiraId();
        List<Change> jira = jiras.get(jiraId);
        if (jira == null) {
            jira = new Vector<Change>();
            jiras.put(jiraId, jira);
        }
        jira.add(change);
    }

    public String getName() {

        return name;
    }

    public Set<String> getJiras() {

        return jiras.keySet();
    }

    public int jiraCount() {

        return jiras.size();
    }

    public int changeCount() {

        int count = 0;
        for (List<Change> changes : jiras.values()) {
            count += changes.size();
        }
        return count;
    }

    public int compareTo(HeatMapItem other) {

        int weight = other.jiraCount() - jiraCount();
        return weight == 0 ? other.changeCount() - changeCount() : weight;
    }

    @Override
    public String toString() {

        String str = name + " - " + jiraCount() + " jira(s) and " + changeCount() + " change(s)\n   ";
        for (String jiraId : jiras.keySet()) {
            str += " " + jiraId;
        }
        return str;
    }
}
