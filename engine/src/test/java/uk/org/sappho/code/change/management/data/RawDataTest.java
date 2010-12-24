package uk.org.sappho.code.change.management.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import uk.org.sappho.string.mapping.Mapper;

public class RawDataTest {

    @Test
    public void shouldValidate() {

        RawData rawData = getFakeRawData(true);
        reWireFakeRawData(rawData);
        assertTrue(rawData.isValid());
    }

    @Test
    public void shouldFailToValidateDueToMissingIssueKey() {

        RawData rawData = getFakeRawData(true);
        // the issueKey on the RevisionData will be missing because we haven't done a re-wire so this should always fail to be valid
        assertFalse(rawData.isValid());
    }

    @Test
    public void shouldFailToValidateDueToMixedDataErrors() {

        RawData rawData = getFakeRawData(false);
        assertFalse(rawData.isValid());
    }

    public static RawData getFakeRawData(boolean valid) {

        String revisionKey = "42";
        String project = "LIFE";
        String issueKey = project + "-" + revisionKey;
        String issueSummary = "Add meaning to life.";
        Date revisionDate = new GregorianCalendar().getTime();
        String commitComment = issueKey + ": " + issueSummary;
        String committer = "sappho";
        String changedFile = "/sappho/fragments/Hymn to Aphrodite.txt";
        List<String> changedFiles = new ArrayList<String>();
        if (valid)
            changedFiles.add(changedFile);
        List<String> badPaths = new ArrayList<String>();
        RevisionData revisionData = valid ? new RevisionData(revisionKey, revisionDate, commitComment, committer,
                changedFiles, badPaths) : new RevisionData("", null, "", null, changedFiles, null);
        String issueType = "change";
        String component = "artistry";
        List<String> components = new ArrayList<String>();
        components.add(component);
        String release = "Fragments 0.1";
        List<String> releases = new ArrayList<String>();
        releases.add(release);
        IssueData issueData = new IssueData(issueKey, issueType, issueSummary, revisionDate, revisionDate, committer,
                project, components, releases);
        String warningCategory = "Loss";
        String warning = "Papyrus decays with time";
        WarningList warningList = new WarningList();
        warningList.add(warningCategory, warning);
        RawData rawData = new RawData();
        rawData.putRevisionData(revisionData);
        rawData.putIssueData(issueData);
        rawData.getWarnings().add(warningList);
        return rawData;
    }

    public static void reWireFakeRawData(RawData rawData) {

        Mapper issueKeyMapper = new Mapper() {
            public String map(String commitComment) {
                return commitComment.substring(0, 7);
            }
        };
        rawData.reWire(issueKeyMapper);
    }
}
