package uk.org.sappho.code.change.management.issues;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.change.management.data.mapping.CommitCommentToIssueKeyMapper;
import uk.org.sappho.warnings.WarningsList;

@Singleton
public class SimpleCommitCommentToJiraIssueKeyMapper implements CommitCommentToIssueKeyMapper {

    private final WarningsList warningsList;
    private static final Pattern SIMPLE_JIRA_REGEX = Pattern.compile("^([a-zA-Z]{2,}-\\d+):.*$");

    @Inject
    public SimpleCommitCommentToJiraIssueKeyMapper(WarningsList warningsList) {

        this.warningsList = warningsList;
    }

    public String getIssueKeyFromCommitComment(String revisionKey, String commitComment) {

        String key = null;
        Matcher matcher = SIMPLE_JIRA_REGEX.matcher(commitComment.split("\n")[0]);
        if (matcher.matches()) {
            key = matcher.group(1);
        } else {
            warningsList.add(new IssueKeyNotFoundWarning(revisionKey, commitComment));
        }
        return key;
    }
}
