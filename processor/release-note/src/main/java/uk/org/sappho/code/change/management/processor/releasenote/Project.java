package uk.org.sappho.code.change.management.processor.releasenote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class Project {

    private final Map<String, Issue> issues = new HashMap<String, Issue>();

    public void add(RevisionData revisionData, IssueData issueData) {

        String issueKey = issueData.getIssueKey();
        Issue issue = issues.get(issueKey);
        if (issue == null) {
            issue = new Issue(issueData);
            issues.put(issueKey, issue);
        }
        issue.add(revisionData);
    }

    public List<String> getIssueKeys() {

        List<String> names = new ArrayList<String>(issues.keySet());
        Collections.sort(names);
        return names;
    }

    public Issue getIssue(String issueKey) {

        return issues.get(issueKey);
    }
}
