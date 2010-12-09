package uk.org.sappho.code.change.management.data;

import java.util.List;

public class IssueData {

    private final String issueKey;
    private final String summary;
    private final String subTaskKey;
    private final List<String> releases;

    public IssueData(String issueKey, String summary, String subTaskKey, List<String> releases) {

        this.issueKey = issueKey;
        this.summary = summary;
        this.subTaskKey = subTaskKey;
        this.releases = releases;
    }

    public String getKey() {

        return issueKey;
    }

    public String getSubTaskKey() {

        return subTaskKey;
    }

    public String getSummary() {

        return summary;
    }

    public List<String> getReleases() {

        return releases;
    }
}
