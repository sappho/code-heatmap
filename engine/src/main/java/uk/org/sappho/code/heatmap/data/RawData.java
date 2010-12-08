package uk.org.sappho.code.heatmap.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.org.sappho.code.heatmap.engine.ChangeSet;
import uk.org.sappho.code.heatmap.issues.IssueWrapper;

public class RawData {

    private final Map<String, IssueWrapper> issueWrappers = new HashMap<String, IssueWrapper>();
    private final List<ChangeSet> changeSets = new Vector<ChangeSet>();

    public void add(ChangeSet change) {

        changeSets.add(change);
        IssueWrapper issue = change.getIssue();
        issueWrappers.put(issue.getKey(), issue);
    }

    public void add(List<ChangeSet> changeSets) {

        for (ChangeSet changeSet : changeSets) {
            add(changeSet);
        }
    }

    public List<ChangeSet> getChangeSets() {

        return changeSets;
    }
}
