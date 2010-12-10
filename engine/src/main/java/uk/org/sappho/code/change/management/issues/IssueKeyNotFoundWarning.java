package uk.org.sappho.code.change.management.issues;

import uk.org.sappho.warnings.Warning;

public class IssueKeyNotFoundWarning extends Warning {

    private final String revisionKey;
    private final String commitComment;

    public IssueKeyNotFoundWarning(String revisionKey, String commitComment) {

        this.revisionKey = revisionKey;
        this.commitComment = commitComment;
    }

    @Override
    public String getCategory() {

        return "Issue key not found";
    }

    @Override
    public String toString() {

        return "No Jira issue key found for revision " + revisionKey + " with commit comment \""
                + commitComment.split("\n")[0] + "\"";
    }
}
