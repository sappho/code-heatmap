package uk.org.sappho.code.heatmap.issues;

import java.util.List;

public interface IssueWrapper {

    public String getKey();

    public String getSubTaskKey();

    public String getSummary();

    public List<String> getReleases();

    public int getWeight();
}
