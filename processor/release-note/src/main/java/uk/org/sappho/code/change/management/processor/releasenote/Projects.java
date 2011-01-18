package uk.org.sappho.code.change.management.processor.releasenote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;

public class Projects {

    private final Map<String, Project> projects = new HashMap<String, Project>();

    public void add(RevisionData revisionData, IssueData issueData) {

        String projectName = issueData.getProject();
        Project project = projects.get(projectName);
        if (project == null) {
            project = new Project();
            projects.put(projectName, project);
        }
        project.add(revisionData, issueData);
    }

    public List<String> getProjectNames() {

        List<String> names = new ArrayList<String>(projects.keySet());
        Collections.sort(names);
        return names;
    }

    public Project getProject(String name) {

        return projects.get(name);
    }
}
