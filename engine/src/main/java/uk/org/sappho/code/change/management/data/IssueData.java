package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class IssueData {

    @NotNull
    @NotEmpty
    private String issueKey;
    @NotNull
    @NotEmpty
    private String type;
    @NotNull
    @NotEmpty
    private String summary;
    @NotNull
    @NotEmpty
    private Date createdDate;
    @NotNull
    @NotEmpty
    private Date lastUpdatedDate;
    @NotNull
    @NotEmpty
    private String assignee;
    @NotNull
    @NotEmpty
    private String project;
    @NotNull
    private List<String> components;
    @NotNull
    private List<String> releases;

    public IssueData() {
    }

    public IssueData(String key, String type, String summary, Date createdDate, Date lastUpdatedDate, String assignee,
            String project, List<String> components, List<String> releases) {

        this.issueKey = key;
        this.type = type;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.assignee = assignee;
        this.project = project;
        this.components = components;
        this.releases = releases;
    }

    public String getIssueKey() {

        return issueKey;
    }

    public void setType(String type) {

        this.type = type;
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

    public String getAssignee() {

        return assignee;
    }

    public String getProject() {

        return project;
    }

    public List<String> getComponents() {

        return components;
    }

    public void setReleases(List<String> releases) {

        this.releases = releases;
    }

    public List<String> getReleases() {

        return releases;
    }

    public String getMainRelease() {
        if (releases == null || releases.size() == 0)
            return null;
        else
            return releases.get(0);
    }

    @Override
    public String toString() {

        return "issue " + issueKey + " last updated on " + lastUpdatedDate;
    }
}
