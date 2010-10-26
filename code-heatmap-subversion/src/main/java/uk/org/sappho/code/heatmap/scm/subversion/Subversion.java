package uk.org.sappho.code.heatmap.scm.subversion;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Info2;
import org.tigris.subversion.javahl.LogMessageCallback;
import org.tigris.subversion.javahl.NodeKind;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.RevisionRange;
import org.tigris.subversion.javahl.SVNClient;

import uk.org.sappho.code.heatmap.engine.Change;
import uk.org.sappho.code.heatmap.engine.CodeHeatMapEngine;
import uk.org.sappho.code.heatmap.engine.Filename;
import uk.org.sappho.code.heatmap.engine.HeatMapCollection;
import uk.org.sappho.code.heatmap.scm.SCM;

public class Subversion implements SCM {

    private final String url;
    private final String basePath;
    private final long startRevision;
    private final long endRevision;
    private final SVNClient svnClient = new SVNClient();
    private static final Logger LOG = Logger.getLogger(Subversion.class);

    public Subversion(String url, String basePath, long startRevision, long endRevision) {

        this.url = url;
        this.basePath = basePath;
        this.startRevision = startRevision;
        this.endRevision = endRevision;
    }

    private class SubversionRevision {

        private final ChangePath[] changedPaths;
        private final long revision;
        @SuppressWarnings("unchecked")
        private final Map revprops;

        @SuppressWarnings("unchecked")
        public SubversionRevision(ChangePath[] changedPaths, long revision, Map revprops) {
            this.changedPaths = changedPaths;
            this.revision = revision;
            this.revprops = revprops;
        }

        public ChangePath[] getChangedPaths() {
            return changedPaths;
        }

        public long getRevision() {
            return revision;
        }

        @SuppressWarnings("unchecked")
        public Map getRevprops() {
            return revprops;
        }
    }

    private class LogMessageProcessor implements LogMessageCallback {

        private final List<SubversionRevision> revisions;

        public LogMessageProcessor(List<SubversionRevision> revisions) {

            this.revisions = revisions;
        }

        @SuppressWarnings("unchecked")
        public void singleMessage(ChangePath[] changedPaths, long revision, Map revprops, boolean hasChildren) {

            if (revision != Revision.SVN_INVALID_REVNUM) {
                revisions.add(new SubversionRevision(changedPaths, revision, revprops));
            }
        }
    }

    public void processChanges(HeatMapCollection heatMapCollection) {

        List<SubversionRevision> revisions = new Vector<SubversionRevision>();
        LOG.debug("Attempting to read Subversion history at " + url + basePath + " from rev. " + startRevision
                + " to rev. " + endRevision);
        RevisionRange[] revisionRange = new RevisionRange[] { new RevisionRange(Revision.getInstance(startRevision),
                Revision.getInstance(endRevision)) };
        String[] revProps = new String[] { "svn:log" };
        try {
            svnClient.logMessages(url + basePath, Revision.getInstance(endRevision), revisionRange,
                    false, true, true, revProps, 0, new LogMessageProcessor(revisions));
        } catch (ClientException e) {
            LOG.error("Unable to read Subversion history at " + url + basePath + " from rev. " + startRevision
                    + " to rev. " + endRevision, e);
        }
        for (SubversionRevision revision : revisions) {
            String comment = (String) revision.getRevprops().get("svn:log");
            LOG.debug("Processing rev. " + revision.getRevision() + " " + comment);
            List<Filename> changedFiles = new Vector<Filename>();
            for (ChangePath changePath : revision.getChangedPaths()) {
                String filename = changePath.getPath();
                try {
                    Revision revisionId = Revision.getInstance(revision.getRevision());
                    Info2[] info = svnClient.info2(url + filename, revisionId, revisionId, false);
                    if (info.length == 1 && info[0].getKind() == NodeKind.file) {
                        LOG.debug("Processing changed file " + filename);
                        changedFiles.add(new Filename(filename));
                    } else {
                        LOG.debug("Presuming " + filename + " is a directory");
                    }
                } catch (ClientException e) {
                    LOG.debug("Unable to determine type of " + filename + " so presuming it deleted");
                }
            }
            heatMapCollection.update(new Change(Long.toString(revision.getRevision()), comment, changedFiles));
        }
    }

    public static void main(String[] args) {

        // TODO: remove this stuff when tests are written 
        try {
            SCM svn = new Subversion("http://svn.catlin.com/dev", "/projects/Frame/branches/10.06", 86603, 88053);
            CodeHeatMapEngine engine = new CodeHeatMapEngine(svn);
            HeatMapCollection hmc = engine.processChanges();
            LOG.info(hmc.toString());
        } catch (Throwable e) {
            LOG.info("No can do!", e);
        }
    }
}