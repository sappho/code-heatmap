package uk.org.sappho.code.change.management.data;

import java.util.Date;
import java.util.List;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import uk.org.sappho.validation.StringArrayConstraint;

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
    @NotEmpty
    private String priority;
    @NotNull
    @NotEmpty
    private String resolution;
    @NotNull
    @NotEmpty
    private String status;
    @StringArrayConstraint
    private List<String> releases;

    public IssueData() {
    }

    public IssueData(String key, String type, String summary, Date createdDate, Date lastUpdatedDate, String assignee,
            String project, String priority, String resolution, String status, List<String> releases) {

        this.issueKey = key;
        this.type = type;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.assignee = assignee;
        this.project = project;
        this.priority = priority;
        this.resolution = resolution;
        this.status = status;
        this.releases = releases;
    }

    public void setNewData(String assignee, String type, String priority, String resolution, String status) {

        this.assignee = assignee;
        this.type = type;
        this.priority = priority;
        this.resolution = resolution;
        this.status = status;
    }

    public void setReleases(List<String> releases) {

        this.releases = releases;
    }

    public String getIssueKey() {

        return issueKey;
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

    public String getPriority() {

        return priority;
    }

    public String getResolution() {

        return resolution;
    }

    public String getStatus() {

        return status;
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
