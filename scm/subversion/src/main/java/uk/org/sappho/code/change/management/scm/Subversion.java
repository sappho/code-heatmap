package uk.org.sappho.code.change.management.scm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Info2;
import org.tigris.subversion.javahl.LogMessage;
import org.tigris.subversion.javahl.NodeKind;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.warnings.WarningsList;

public class Subversion implements SCM {

    private final SVNClient svnClient = new SVNClient();
    protected WarningsList warningsList;
    private final Configuration config;
    private static final Logger LOG = Logger.getLogger(Subversion.class);
    private static final String START_REV_PROP = "svn.start.rev";

    @Inject
    public Subversion(WarningsList warningsList, Configuration config) {

        LOG.info("Using Subversion SCM plugin");
        this.warningsList = warningsList;
        this.config = config;
    }

    public void scan(RawData rawData) throws SCMException {

        String errorMessage = "Unable to find Subversion session parameters";
        try {
            String url = config.getProperty("svn.url");
            String basePath = config.getProperty("svn.path");
            long endRevision = Long.parseLong(config.getProperty("svn.end.rev", "-1"));
            if (endRevision < 0) {
                try {
                    Info2[] info = svnClient.info2(url + basePath, Revision.HEAD, Revision.HEAD, false);
                    endRevision = info[0].getLastChangedRev();
                } catch (ClientException e) {
                    throw new SCMException("Unable to determine head revision of " + url + basePath, e);
                }
            }
            long startRevision = Long
                    .parseLong(config.getProperty(START_REV_PROP, Long.toString(endRevision - 49)));
            errorMessage = "Unable to read Subversion history for " + url + basePath + " from revision "
                    + startRevision + " to revision " + endRevision;
            if (endRevision < startRevision) {
                LOG.info("Unable to read Subversion history for " + url + basePath + " from revision " + startRevision
                        + " to revision " + endRevision
                        + " - if incrememntal then this probably means there are no new revisions");
            } else {
                Map<String, Integer> nodeKindCache = new HashMap<String, Integer>();
                int nodeCount = 0;
                int fileCount = 0;
                int deleteCount = 0;
                int nodeKindCacheHits = 0;
                LOG.info("Reading Subversion history for " + url + basePath + " from revision " + startRevision
                        + " to revision " + endRevision);
                LogMessage[] logMessages = svnClient.logMessages(url + basePath, Revision.getInstance(startRevision),
                        Revision.getInstance(endRevision), false, true);
                LOG.info("Starting to process " + logMessages.length + " revisions");
                int revisionCount = 0;
                for (LogMessage logMessage : logMessages) {
                    long revisionNumber = logMessage.getRevisionNumber();
                    Date date = logMessage.getDate();
                    String commitComment = logMessage.getMessage();
                    List<String> changedFiles = new Vector<String>();
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
                                    } else {
                                        warningsList.add(new SubversionPathWarning(path));
                                    }
                                }
                            }
                            if (nodeKind == NodeKind.file) {
                                changedFiles.add(path);
                                fileCount++;
                            }
                        } else {
                            deleteCount++;
                        }
                    }
                    String revisionKey = Long.toString(revisionNumber);
                    if (changedFiles.size() > 0) {
                        rawData.putRevisionData(new RevisionData(revisionKey, date, commitComment, changedFiles));
                        revisionCount++;
                    } else {
                        warningsList.add(new RevisionHasNoChangesWarning(revisionKey));
                    }
                }
                LOG.info("Processed " + revisionCount + " revisions");
                LOG.info("Stats: " + nodeCount + " nodes " + fileCount + " files " + deleteCount + " deletes "
                        + nodeKindCacheHits + " cache hits");
                config.takeSnapshot();
                config.setProperty(START_REV_PROP, "" + ++endRevision);
                config.saveChanged(START_REV_PROP + ".save.filename");
            }
        } catch (Throwable t) {
            throw new SCMException(errorMessage, t);
        }
    }

    public int compare(RevisionData changeData1, RevisionData changeData2) {

        long revision1 = Long.parseLong(changeData1.getKey());
        long revision2 = Long.parseLong(changeData2.getKey());
        int comparison = (revision1 == revision2) ? 0 : (revision1 > revision2) ? 1 : -1;
        return comparison;
    }
}
