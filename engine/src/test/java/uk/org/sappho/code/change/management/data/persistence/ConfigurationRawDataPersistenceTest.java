package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.warnings.SimpleWarningList;
import uk.org.sappho.warnings.WarningList;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationRawDataPersistenceTest {

    @Mock
    private Configuration mockConfiguration;

    @Test
    public void shouldWriteToZipFileAndThenReadBackSameData() throws ConfigurationException, IOException {

        String targetFilename = "target/ConfigurationRawDataPersistenceTest-raw-data.zip";

        new File(targetFilename).delete();

        String filename = mockConfiguration.getProperty("raw.data.store.filename");
        when(filename).thenReturn(targetFilename);

        String revisionKey = "42";
        String project = "LIFE";
        String issueKey = project + "-42";
        String issueSummary = "Add meaning to life.";
        Date revisionDate = new GregorianCalendar().getTime();
        String commitComment = issueKey + ": " + issueSummary;
        String committer = "sappho";
        String changedFile = "/sappho/sappho.txt";
        List<String> changedFiles = new ArrayList<String>();
        changedFiles.add(changedFile);
        List<String> badPaths = new ArrayList<String>();
        RevisionData revisionData = new RevisionData(revisionKey, revisionDate, commitComment, committer, changedFiles,
                badPaths);
        String issueType = "change";
        String component = "Philosophy";
        List<String> components = new ArrayList<String>();
        components.add(component);
        String release = "Life 1.0";
        List<String> releases = new ArrayList<String>();
        releases.add(release);
        IssueData issueData = new IssueData(issueKey, issueType, issueSummary, revisionDate, revisionDate, committer,
                project, components, releases);
        String warningCategory = "Mishaps";
        String warning = "Poison taken in Athens";
        WarningList warningList = new SimpleWarningList();
        warningList.add(warningCategory, warning);
        RawData rawData = new RawData();
        rawData.putRevisionData(revisionData);
        rawData.putIssueData(issueData);
        rawData.putWarnings(warningList);

        ConfigurationRawDataPersistence configurationRawDataPersistence = new ConfigurationRawDataPersistence(
                mockConfiguration);
        configurationRawDataPersistence.save(rawData);

        assertTrue(new File(targetFilename).exists());
    }
}
