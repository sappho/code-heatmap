package uk.org.sappho.code.heatmap.engine;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Change {

    private static final Pattern JIRA_REGEX = Pattern.compile("^([A-Z]+-[0-9]+):.*$");
    private final String revisionId;
    private final String comment;
    private final List<Filename> changedFiles;

    public Change(String revisionId, String comment, List<Filename> changedFiles) {

        this.revisionId = revisionId;
        this.comment = comment;
        this.changedFiles = changedFiles;
    }

    public String getRevisionId() {

        return revisionId;
    }

    public String getComment() {

        return comment;
    }

    public String getJiraId() {

        String jiraId = "----";
        Matcher matcher = JIRA_REGEX.matcher(comment.split("\n")[0]);
        if (matcher.matches()) {
            jiraId = matcher.group(1);
        }
        return jiraId;
    }

    public final List<Filename> getChangedFiles() {

        return changedFiles;
    }
}
