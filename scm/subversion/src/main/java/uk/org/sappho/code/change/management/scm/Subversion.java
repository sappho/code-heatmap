package uk.org.sappho.code.change.management.scm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Info2;
import org.tigris.subversion.javahl.LogMessage;
import org.tigris.subversion.javahl.NodeKind;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.ChangedFile;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.configuration.Configuration;

public class Subversion implements SCM {

    private final SVNClient svnClient = new SVNClient();
    private final Configuration config;
    private static final Logger log = Logger.getLogger(Subversion.class);
    private static final String startRevPropProperty = "svn.start.rev";

    @Inject
    public Subversion(Configuration config) {

        log.info("Using Subversion SCM plugin");
        this.config = config;
    }

    public void scan(RawData rawData) throws SCMException {

        String errorMessage = "Unable to get Subversion session parameters";
        try {
            String url = config.getProperty("svn.url");
            String basePath = config.getProperty("svn.path");
            String targetURL = url + basePath;
            long startRevision = Long.parseLong(config.getProperty(startRevPropProperty));
            long endRevision = Long.parseLong(config.getProperty("svn.end.rev", "-1"));
            if (endRevision < 0) {
                try {
                    Info2[] info = svnClient.info2(targetURL, Revision.HEAD, Revision.HEAD, false);
                    endRevision = info[0].getLastChangedRev();
                } catch (ClientException e) {
                    throw new SCMException("Unable to determine head revision of " + targetURL, e);
                }
            }
            errorMessage = "Unable to read Subversion history for " + targetURL + " from revision "
                    + startRevision + " to revision " + endRevision;
            if (endRevision < startRevision) {
                log.info("Unable to read Subversion history for " + targetURL + " from revision " + startRevision
                        + " to revision " + endRevision
                        + " - if incrememntal then this probably means there are no new revisions");
            } else {
                RevisionDataPostProcessor revisionDataPostProcessor = (RevisionDataPostProcessor) config
                        .getGroovyScriptObject("svn.revision.post.processor");
                Map<String, Integer> nodeKindCache = new HashMap<String, Integer>();
                long nodeCount = 0;
                long fileCount = 0;
                long addCount = 0;
                long deleteCount = 0;
                long badPathCount = 0;
                long noFilesCount = 0;
                long nodeKindCacheAdds = 0;
                long nodeKindCacheHits = 0;
                log.info("Reading Subversion history for " + targetURL + " from revision " + startRevision
                        + " to revision " + endRevision);
                LogMessage[] logMessages = svnClient.logMessages(targetURL, Revision.getInstance(startRevision),
                        Revision.getInstance(endRevision), false, true);
                log.info("Starting to process " + logMessages.length + " revisions");
                long revisionCount = 0;
                for (LogMessage logMessage : logMessages) {
                    if (++revisionCount % 250 == 0) {
                        log.info("Processed " + revisionCount + " revisions so far");
                    }
                    long revisionNumber = logMessage.getRevisionNumber();
                    Date date = logMessage.getDate();
                    String commitComment = logMessage.getMessage();
                    String committer = logMessage.getAuthor();
                    List<ChangedFile> changedFiles = new ArrayList<ChangedFile>();
                    for (ChangePath changePath : logMessage.getChangedPaths()) {
                        nodeCount++;
                        String path = changePath.getPath();
                        char action = changePath.getAction();
                        if (action != 'D') {
                            int nodeKind = changePath.getNodeKind();
                            if (nodeKind == NodeKind.unknown) {
                                Integer nodeKindObj = nodeKindCache.get(path);
                                if (nodeKindObj != null) {
                                    nodeKind = nodeKindObj;
                                    nodeKindCacheHits++;
                                } else {
                                    Revision revisionId = Revision.getInstance(revisionNumber);
                                    Info2[] info = svnClient.info2(url + path, revisionId, revisionId, false);
                                    if (info.length == 1) {
                                        nodeKind = info[0].getKind();
                                        nodeKindCache.put(path, nodeKind);
                                        nodeKindCacheAdds++;
                                    } else {
                                        rawData.getWarnings().add("Bad path",
                                                "Unable to get Subversion data for path " + path);
                                        badPathCount++;
                                    }
                                }
                            }
                            if (nodeKind == NodeKind.file) {
                                boolean added = action == 'A';
                                if (added)
                                    addCount++;
                                changedFiles.add(new ChangedFile(path, added));
                                fileCount++;
                            }
                        } else {
                            deleteCount++;
                        }
                    }
                    String revisionKey = Long.toString(revisionNumber);
                    if (changedFiles.size() == 0) {
                        noFilesCount++;
                    }
                    RevisionData revisionData = new RevisionData(revisionKey, date, commitComment, committer,
                            changedFiles);
                    revisionDataPostProcessor.process(revisionData);
                    rawData.putRevisionData(revisionData);
                }
                log.info("Processed " + revisionCount + " revisions");
                log.info("Stats: " + nodeCount + " nodes, " + fileCount + " files, " + addCount + " adds, "
                        + deleteCount + " deletes, " + badPathCount + " bad paths, " + noFilesCount
                        + " revisions with no file changes, " + nodeKindCacheAdds + " cache adds, " + nodeKindCacheHits
                        + " cache hits");
                config.takeSnapshot();
                config.setProperty(startRevPropProperty, "" + ++endRevision);
                config.saveChanged(startRevPropProperty + ".save.filename");
            }
        } catch (Throwable t) {
            throw new SCMException(errorMessage, t);
        }
    }
}
