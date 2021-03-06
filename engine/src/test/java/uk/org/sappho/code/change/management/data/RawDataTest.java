package uk.org.sappho.code.change.management.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

public class RawDataTest {

    @Test
    public void shouldValidate() {

        RawData rawData = getFakeRawData();
        Warnings warningList = rawData.getWarnings();
        warningList.add("Test 1", "Example warning");
        warningList.add("Test 1", "Another example warning");
        assertTrue(rawData.isValid());
        assertTrue(warningList.getCategories().size() == 1);
        assertTrue(warningList.getWarnings("Test 1").size() == 2);
        warningList.add("Test 2", "Another example warning in a new category");
        assertTrue(rawData.isValid());
        assertTrue(warningList.getCategories().size() == 2);
        assertTrue(warningList.getWarnings("Test 1").size() == 2);
        assertTrue(warningList.getWarnings("Test 2").size() == 1);
        assertTrue(rawData.isValid());
    }

    @Test
    public void shouldFailValidationDueToNullWarningCategory() {

        RawData rawData = getFakeRawData();
        assertTrue(rawData.isValid());
        rawData.getWarnings().add(null, "Example warning");
        assertFalse(rawData.isValid());
    }

    // TODO: fix this @Test
    public void shouldFailValidationDueToEmptyWarning() {

        RawData rawData = getFakeRawData();
        assertTrue(rawData.isValid());
        rawData.getWarnings().add("Empty test", "");
        assertFalse(rawData.isValid());
    }

    @Test
    public void shouldFailValidationDueToEmptyCategoryAndWarning() {

        RawData rawData = getFakeRawData();
        assertTrue(rawData.isValid());
        rawData.getWarnings().add("", "");
        assertFalse(rawData.isValid());
    }

    @Test
    public void shouldFailValidationDueToNullCategoryAndWarning() {

        RawData rawData = getFakeRawData();
        assertTrue(rawData.isValid());
        rawData.getWarnings().add(null, null);
        assertFalse(rawData.isValid());
    }

    @Test
    public void shouldFailValidationDueToMixedWarningFaults() {

        RawData rawData = getFakeRawData();
        assertTrue(rawData.isValid());
        Warnings warningList = rawData.getWarnings();
        warningList.add("Test 1", "Example warning");
        warningList.add(null, null);
        warningList.add("Test 1", "Another example warning");
        warningList.add(null, "Example warning");
        warningList.add("", "Warning with blank category");
        warningList.add("Test 2", "Another example warning in a new category");
        warningList.add("Empty test", "");
        assertFalse(rawData.isValid());
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
        List<ChangedFile> changedFiles = new ArrayList<ChangedFile>();
        changedFiles.add(new ChangedFile(changedFile, false));
        RevisionData revisionData = new RevisionData(revisionKey, revisionDate, commitComment, committer, changedFiles);
        revisionData.setIssueKey(issueKey);
        String issueType = "change";
        String priority = "medium";
        String resolution = "fixed";
        String status = "closed";
        String release = "Fragments 0.1";
        List<String> releases = new ArrayList<String>();
        releases.add(release);
        IssueData issueData = new IssueData(issueKey, issueType, issueSummary, revisionDate, revisionDate, committer,
                project, priority, resolution, status, releases);
        RawData rawData = new RawData();
        rawData.putRevisionData(revisionData);
        rawData.putIssueData(issueKey, issueData);
        return rawData;
    }
}
