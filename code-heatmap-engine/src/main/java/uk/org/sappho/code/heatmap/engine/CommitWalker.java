package uk.org.sappho.code.heatmap.engine;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.LogMessageCallback;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.RevisionRange;
import org.tigris.subversion.javahl.SVNClient;

public class CommitWalker {

    public static final String DIRECTORY = "Directories";
    public static final String FILENAME = "File names";
    public static final String FULLFILENAME = "Full file names";
    public static final String CLASSNAME = "Class names";
    public static final String PACKAGENAME = "Package names";
    public static final String[] HEATMAPS = { DIRECTORY, FILENAME, FULLFILENAME, CLASSNAME, PACKAGENAME };

    private final SVNClient svnClient = new SVNClient();
    private final String url;
    private final long startRevision;
    private final long endRevision;
    private static final Pattern JIRA_REGEX = Pattern.compile("^([A-Z]+-[0-9]+):.*$");
    private static final Pattern JAVA_REGEX = Pattern
            .compile("^.+?/((com|org|net|edu|gov|mil|biz|info)/.+)/(.+?)\\.java$");
    private static final Logger LOG = Logger.getLogger(CommitWalker.class);

    public CommitWalker(String url, long startRevision, long endRevision) {

        this.url = url;
        this.startRevision = startRevision;
        this.endRevision = endRevision;
    }

    public class ChangedFileException extends Exception {

        private static final long serialVersionUID = -2624802816685615545L;

        public ChangedFileException(String message) {

            super(message);
        }
    }

    public class ChangedFile {

        private final String filename;
        private final Matcher javaMatcher;
        private final char action;

        public ChangedFile(String filename, char action) {

            this.filename = filename;
            javaMatcher = JAVA_REGEX.matcher(filename);
            this.action = action;
        }

        public String getFullFilename() {

            return filename;
        }

        public String getFilename() {

            return new File(filename).getName();
        }

        public String getDirectory() {

            return new File(filename).getParent();
        }

        public String getPackageName() throws ChangedFileException {

            checkJava();
            return javaMatcher.group(1).replace('/', '.');
        }

        public String getClassName() throws ChangedFileException {

            checkJava();
            return javaMatcher.group(3);
        }

        private void checkJava() throws ChangedFileException {

            if (!isJava()) {
                throw new ChangedFileException("Not a Java class");
            }
        }

        public boolean isJava() {

            return javaMatcher.matches();
        }

        public char getAction() {

            return action;
        }
    }

    public class LogData implements LogMessageCallback {

        private final List<Commit> commits = new Vector<Commit>();
        private final Map<String, HeatMap> heatMaps = new HashMap<String, HeatMap>();

        public class Commit {

            private final long revision;
            private final String commitComment;
            private final List<ChangedFile> changedFiles = new Vector<ChangedFile>();

            public Commit(long revision, String commitComment, ChangePath[] changedPaths) {

                this.revision = revision;
                this.commitComment = commitComment;
                if (changedPaths != null) {
                    for (ChangePath path : changedPaths) {
                        changedFiles.add(new ChangedFile(path.getPath(), path.getAction()));
                    }
                }
            }

            public long getRevision() {

                return revision;
            }

            public String getCommitComment() {

                return commitComment;
            }

            public String getJiraId() {

                String jiraId = "----";
                Matcher matcher = JIRA_REGEX.matcher(commitComment.split("\n")[0]);
                if (matcher.matches()) {
                    jiraId = matcher.group(1);
                }
                return jiraId;
            }

            public final List<ChangedFile> getChangedFiles() {

                return changedFiles;
            }
        }

        public class HeatMapItem implements Comparable<HeatMapItem> {

            private final String name;
            private final Map<String, List<Commit>> jiras = new HashMap<String, List<Commit>>();

            public HeatMapItem(String name) {

                this.name = name;
            }

            private void update(Commit commit) {

                String jiraId = commit.getJiraId();
                List<Commit> jira = jiras.get(jiraId);
                if (jira == null) {
                    jira = new Vector<Commit>();
                    jiras.put(jiraId, jira);
                }
                jira.add(commit);
            }

            @Override
            public String toString() {

                String str = name + " - " + jiraCount() + " jira(s) and " + commitCount() + " commit(s)\n   ";
                for (String jiraId : jiras.keySet()) {
                    str += " " + jiraId;
                }
                return str;
            }

            public int jiraCount() {

                return jiras.size();
            }

            public int commitCount() {

                int count = 0;
                for (List<Commit> commits : jiras.values()) {
                    count += commits.size();
                }
                return count;
            }

            public int compareTo(HeatMapItem other) {

                int weight = other.jiraCount() - jiraCount();
                return weight == 0 ? other.commitCount() - commitCount() : weight;
            }
        }

        public class HeatMap {

            private final String name;
            private final Map<String, HeatMapItem> heatMap = new HashMap<String, HeatMapItem>();

            public HeatMap(String name) {

                this.name = name;
                heatMaps.put(name, this);
            }

            private void update(String name, Commit commit) {

                HeatMapItem item = heatMap.get(name);
                if (item == null) {
                    item = new HeatMapItem(name);
                    heatMap.put(name, item);
                }
                item.update(commit);
            }

            public List<HeatMapItem> getSortedList() {

                List<HeatMapItem> list = new Vector<HeatMapItem>(heatMap.values());
                Collections.sort(list);
                return list;
            }

            @Override
            public String toString() {

                String str = "------------------------------------------------\n" + name
                        + "\n------------------------------------------------\n\n";
                for (HeatMapItem item : getSortedList()) {
                    str += item.toString() + "\n";
                }
                return str;
            }
        }

        public LogData() {

            for (String name : HEATMAPS) {
                new HeatMap(name);
            }
        }

        private void updateHeatMap(String heatMapKey, String name, Commit commit) {

            HeatMap heatMap = getHeatMap(heatMapKey);
            heatMap.update(name, commit);
        }

        @SuppressWarnings("unchecked")
        public void singleMessage(ChangePath[] changedPaths, long revision, Map revprops, boolean hasChildren) {

            if (revision != Revision.SVN_INVALID_REVNUM) {
                Commit commit = new Commit(revision, (String) revprops.get("svn:log"), changedPaths);
                // Issue issue = jira.getIssue(commit.getJira());
                commits.add(commit);
                for (ChangedFile changedFile : commit.getChangedFiles()) {
                    updateHeatMap(DIRECTORY, changedFile.getDirectory(), commit);
                    updateHeatMap(FILENAME, changedFile.getFilename(), commit);
                    updateHeatMap(FULLFILENAME, changedFile.getFullFilename(), commit);
                    if (changedFile.isJava()) {
                        try {
                            updateHeatMap(CLASSNAME, changedFile.getClassName(), commit);
                            updateHeatMap(PACKAGENAME, changedFile.getPackageName(), commit);
                        } catch (ChangedFileException e) {
                            // because of isJava() check these shouldn't throw an exception
                            // we're not allowed to throw exceptions out of this callback method so we have to use this try block
                        }
                    }
                }
            }
        }

        public final List<Commit> getCommits() {

            return commits;
        }

        public final HeatMap getHeatMap(String name) {

            return heatMaps.get(name);
        }

        @Override
        public String toString() {

            String str = "";
            for (String name : HEATMAPS) {
                str += "\n" + getHeatMap(name).toString();
            }
            return str;
        }
    }

    public final LogData extract() throws Exception {

        LogData logData = new LogData();
        RevisionRange[] revisionRange = new RevisionRange[] { new RevisionRange(Revision.getInstance(startRevision),
                Revision.getInstance(endRevision)) };
        String[] revProps = new String[] { "svn:log" };
        svnClient.logMessages(url, Revision.getInstance(endRevision), revisionRange, false, true, false,
                revProps, 0, logData);
        return logData;
    }

    public static void main(String[] args) {

    	// TODO: remove this stuff when tests are written 
        try {
            CommitWalker walker = new CommitWalker("http://svn.catlin.com/dev/projects/Frame/branches/10.06",
                    86603, 88053);
            LogData data = walker.extract();
            LOG.info(data.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
