package uk.org.sappho.code.heatmap.scm;

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

import uk.org.sappho.code.change.management.data.ChangeSet;
import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.issues.IssueManagement;
import uk.org.sappho.code.change.management.scm.SCM;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.warnings.Warnings;

public class Subversion implements SCM {

    private final SVNClient svnClient = new SVNClient();
    protected Warnings warnings;
    private final Configuration config;
    private final IssueManagement issueManagement;
    private static final Logger LOG = Logger.getLogger(Subversion.class);
    private static final String START_REV_PROP = "svn.start.rev";

    @Inject
    public Subversion(Warnings warnings, Configuration config, IssueManagement issueManagement) {

        LOG.info("Using Subversion SCM plugin");
        this.warnings = warnings;
        this.config = config;
        this.issueManagement = issueManagement;
    }

    public List<ChangeSet> scan() throws SCMException {

        List<ChangeSet> changeSets = new Vector<ChangeSet>();
        String errorMessage = "Unable to find Subversion session parameters";
        try {
            String url = config.getProperty("svn.url");
            String basePath = config.getProperty("svn.path");
            long endRevision = Long.parseLong(config.getProperty("svn.end.rev", "-1"));
            if (endRevision < 0) {
                try {
                    Info2[] info = svnClient.info2(url + basePath, Revision.HEAD, Revision.HEAD, false);
                    endRevision = info[0].getLastChangedRev();
                    LOG.debug("Using HEAD revision because svn.end.rev property requires it");
                } catch (ClientException e) {
                    LOG.error("Unable to determine head revision of " + url + basePath, e);
                }
            }
            long startRevision = Long
                    .parseLong(config.getProperty(START_REV_PROP, Long.toString(endRevision - 49)));
            errorMessage = "Unable to read Subversion history for " + url + basePath + " from rev. " + startRevision
                    + " to rev. " + endRevision;
            LOG.debug("Subversion history scan parameters:");
            LOG.debug("url:           " + url);
            LOG.debug("basePath:      " + basePath);
            LOG.debug("startRevision: " + startRevision);
            LOG.debug("endRevision:   " + endRevision);
            if (endRevision < startRevision) {
                LOG.info("Unable to read Subversion history for " + url + basePath + " from rev. " + startRevision
                        + " to rev. " + endRevision
                        + " - if incrememntal then this probably means there are no new revisions");
            } else {
                issueManagement.init();
                Map<String, Integer> nodeKindCache = new HashMap<String, Integer>();
                int nodeKindCacheHits = 0;
                LOG.info("Reading Subversion history for " + url + basePath + " from rev. " + startRevision
                        + " to rev. " + endRevision);
                LogMessage[] logMessages = svnClient.logMessages(url + basePath, Revision.getInstance(startRevision),
                        Revision.getInstance(endRevision), false, true);
                LOG.info("Processing " + logMessages.length + " revisions");
                int revisionCount = 0;
                for (LogMessage logMessage : logMessages) {
                    long revisionNumber = logMessage.getRevisionNumber();
                    String commitComment = logMessage.getMessage();
                    IssueData issue = issueManagement.getIssue(commitComment);
                    if (issue != null) {
                        LOG.debug("Processing rev. " + revisionNumber + " " + commitComment);
                        List<String> changedFiles = new Vector<String>();
                        for (ChangePath changePath : logMessage.getChangedPaths()) {
                            String path = changePath.getPath();
                            if (changePath.getAction() != 'D') {
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
                                            warnings.add(new SubversionPathWarning(path));
                                        }
                                    }
                                }
                                switch (nodeKind) {
                                case NodeKind.file:
                                    LOG.debug("Processing changed file " + path);
                                    changedFiles.add(path);
                                    break;
                                case NodeKind.dir:
                                    LOG.debug("Path " + path + " is a directory so not including it");
                                    break;
                                default:
                                    LOG.debug("Path " + path + " is of unknown type so not including it");
                                }
                            } else {
                                LOG.debug("Path " + path + " is deleted so not including it");
                            }
                        }
                        changeSets
                                .add(new ChangeSet(Long.toString(revisionNumber), commitComment, issue, changedFiles));
                        revisionCount++;
                    }
                }
                LOG.info("Added " + revisionCount + " revisions to heat maps with " + nodeKindCacheHits
                        + " node kind cache hits");
                config.takeSnapshot();
                config.setProperty(START_REV_PROP, "" + ++endRevision);
                config.saveChanged(START_REV_PROP + ".save.filename");
            }
        } catch (Throwable t) {
            throw new SCMException(errorMessage, t);
        }
        return changeSets;
    }
}
