package uk.org.sappho.code.change.management.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class RawData {

    private final Map<String, IssueData> issueWrappers = new HashMap<String, IssueData>();
    private final List<ChangeSet> changeSets = new Vector<ChangeSet>();

    public void add(ChangeSet change) {

        changeSets.add(change);
        IssueData issue = change.getIssue();
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
