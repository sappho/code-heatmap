package uk.org.sappho.code.change.management.processor.releasenote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class Issue {

    private final IssueData issueData;
    private final Map<String, RevisionData> revisions = new HashMap<String, RevisionData>();

    public Issue(IssueData issueData) {

        this.issueData = issueData;
    }

    public void add(RevisionData revisionData) {

        revisions.put(revisionData.getRevisionKey(), revisionData);
    }

    public IssueData getIssueData() {

        return issueData;
    }

    public List<String> getRevisionKeys() {

        List<String> names = new ArrayList<String>(revisions.keySet());
        Collections.sort(names);
        return names;
    }

    public RevisionData getRevisionData(String revisionKey) {

        return revisions.get(revisionKey);
    }
}
