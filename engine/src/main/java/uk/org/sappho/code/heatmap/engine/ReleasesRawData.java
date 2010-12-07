package uk.org.sappho.code.heatmap.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class ReleasesRawData {

    private final Map<String, IssueWrapper> issues = new HashMap<String, IssueWrapper>();
    private final List<ChangeSet> changes = new Vector<ChangeSet>();

    public void update(ChangeSet change) {

        changes.add(change);
        IssueWrapper issue = change.getIssue();
        issues.put(issue.getKey(), issue);
    }

    public List<ChangeSet> getChanges() {

        return changes;
    }
}
