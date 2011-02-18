package uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis;

import static ch.lambdaj.Lambda.join;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static uk.org.sappho.codeheatmap.ui.web.server.handlers.utils.Normalisers.normaliseFilename;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis.CommitterStats;

public class BadCommitters {

    public static final String DEFECT = "defect";
    public static final String CHANGE = "change";
    public static final String TASK = "task";

    Map<String, Stats> data = new HashMap<String, Stats>();
    Map<String, String> fileCreators = new HashMap<String, String>();

    public void addChangeSet(List<String> changedFiles, Date commitDate, String committer, String issueKey,
            String issueType) {
        for (String file : changedFiles) {
            addChange(file, commitDate, normaliseCommitter(committer), issueKey, issueType);
        }
    }

    private String normaliseCommitter(String committer) {
        if (committer.startsWith("CATLIN\\")) {
            return committer.substring(7).toLowerCase();
        }
        return committer.toLowerCase();
    }

    private void addChange(String file, Date commitDate, String committer, String issueKey, String issueType) {
        if (unrecognisedIssueType(issueType)) {
            throw new RuntimeException("Unrecognised issue type: " + issueType);
        }
        if (!file.endsWith(".java") || file.contains("src/test/java")) {
            return;
        }
        String normalisedFile = normaliseFilename(file);
        Stats thisCommitterStats = getStatsForCommitter(committer);
        if (!fileCreators.containsKey(normalisedFile) && issueType.equals(CHANGE)) {
            thisCommitterStats.scoreNewFile(normalisedFile);
            fileCreators.put(normalisedFile, committer);
        } else {
            if (issueType.equals(DEFECT)) {
                String whoCreatedIt = fileCreators.get(normalisedFile);
                if (whoCreatedIt == null) {
                    // nobody created, don't score it
                    return;
                }
                data.get(whoCreatedIt).scoreDefect(issueKey);
            } else {
                //                thisCommitterStats.scoreNewFile(normalisedFile);
            }
        }

    }

    private boolean unrecognisedIssueType(String issueType) {
        return issueType == null
                || (!issueType.equals(DEFECT)
                        && !issueType.equals(CHANGE)
                && !issueType.equals(TASK));
    }

    private Stats getStatsForCommitter(String committer) {
        Stats thisCommitterStats = data.get(committer);
        if (thisCommitterStats == null) {
            thisCommitterStats = new Stats();
            data.put(committer, thisCommitterStats);
        }
        return thisCommitterStats;
    }

    public Set<String> getCommitters() {
        return data.keySet();
    }

    public Integer getFilesCreatedOrChanged(String committer) {
        return data.get(committer).getNewFilesCount();
    }

    public Integer getDefectsCaused(String committer) {
        return data.get(committer).getDefectsCausedCount();
    }

    public List<CommitterStats> getList() {
        List<CommitterStats> result = new ArrayList<CommitterStats>();
        for (String committer : data.keySet()) {
            result.add(new CommitterStats(committer,
                    getFilesCreatedOrChanged(committer),
                    getDefectsCaused(committer),
                    getResponsibilities(committer)));
        }
        return sort(result, on(CommitterStats.class).getRatioNegated());
    }

    private String getResponsibilities(String committer) {
        return join(data.get(committer).getFiles(), "\n");
    }

    public static class Stats {

        private int newFiles;
        private int defectsCaused;
        private final Set<String> issueKeys = new HashSet<String>();
        private final Set<String> files = new HashSet<String>();

        public Stats() {
            newFiles = 0;
            defectsCaused = 0;
        }

        public Set<String> getFiles() {
            return files;
        }

        public int getNewFilesCount() {
            return newFiles;
        }

        public int getDefectsCausedCount() {
            return defectsCaused;
        }

        public void scoreDefect(String issueKey) {
            if (!issueKeys.contains(issueKey)) {
                issueKeys.add(issueKey);
                defectsCaused++;
            }
        }

        public void scoreNewFile(String file) {
            if (!files.contains(file)) {
                files.add(file);
                newFiles++;
            }
        }
    }

    public Map<String, String> getFileCreators() {
        return fileCreators;
    }

}
