package uk.org.sappho.code.change.management.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.org.sappho.string.mapping.Mapper;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.WarningList;

public class RawDataTest {

    private RawData rawData;

    @Before
    public void setupRawData() {

        rawData = getFakeRawData();
    }

    @Test
    public void shouldValidate() {

        assertTrue(rawData.isValid());
    }

    @Test
    public void shouldFailToValidateDueToInvalidWarning() {

        rawData.getWarnings().add(null, null);
        boolean valid = rawData.isValid();
        assertFalse(valid);
    }

    public static RawData getFakeRawData() {

        String revisionKey = "42";
        String project = "LIFE";
        String issueKey = project + "-" + revisionKey;
        String issueSummary = "Add meaning to life.";
        Date revisionDate = new GregorianCalendar().getTime();
        String commitComment = issueKey + ": " + issueSummary;
        String committer = "sappho";
        String changedFile = "/sappho/fragments/Hymn to Aphrodite.txt";
        List<String> changedFiles = new ArrayList<String>();
        changedFiles.add(changedFile);
        List<String> badPaths = new ArrayList<String>();
        RevisionData revisionData = new RevisionData(revisionKey, revisionDate, commitComment, committer, changedFiles,
                badPaths);
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
        WarningList warningList = new SimpleWarningList();
        warningList.add(warningCategory, warning);
        RawData rawData = new RawData();
        rawData.putRevisionData(revisionData);
        rawData.putIssueData(issueData);
        rawData.putWarnings(warningList);
        Mapper issueKeyMapper = new Mapper() {
            public String map(String commitComment) {
                return commitComment.substring(0, 7);
            }
        };
        rawData.reWire(issueKeyMapper);
        return rawData;
    }
}
