package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class IssueData {

    private String key;
    private String type;
    private String summary;
    private Date createdDate;
    private Date lastUpdatedDate;
    private List<String> components;
    private List<String> releases;
    private final List<String> subTaskKeys = new Vector<String>();

    public IssueData() {
    }

    public IssueData(String key, String type, String summary, Date createdDate, Date lastUpdatedDate,
            List<String> components, List<String> releases) {

        this.key = key;
        this.type = type;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.components = components;
        this.releases = releases;
    }

    public String getKey() {

        return key;
    }

    public String getType() {

        return type;
    }

    public String getSummary() {

        return summary;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public Date getLastUpdatedDate() {

        return lastUpdatedDate;
    }

    public List<String> getComponents() {

        return components;
    }

    public List<String> getReleases() {

        return releases;
    }

    public void putSubTaskKey(String issueKey) {

        if (!subTaskKeys.contains(issueKey)) {
            subTaskKeys.add(issueKey);
        }
    }

    public List<String> getSubTaskKeys() {

        return subTaskKeys;
    }
}
