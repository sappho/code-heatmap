package uk.org.sappho.code.change.management.processor.releasenote;

import java.util.HashMap;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class Project {

    private final String projectName;
    private final Map<String, Issue> issues = new HashMap<String, Issue>();

    public Project(String projectName) {

        this.projectName = projectName;
    }

    public void add(RevisionData revisionData, IssueData issueData) {

    }
}
