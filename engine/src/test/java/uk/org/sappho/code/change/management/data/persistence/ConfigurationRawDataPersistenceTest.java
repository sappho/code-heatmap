package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
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

    private RawData rawData;

    @Before
    public void setupRawData() {

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
        rawData = new RawData();
        rawData.putRevisionData(revisionData);
        rawData.putIssueData(issueData);
        rawData.putWarnings(warningList);
    }

    @Test
    public void shouldWriteToZipFileAndThenReadBackSameData() throws ConfigurationException, IOException {

        writeToFileAndThenReadBackSameData("zip");
    }

    @Test
    public void shouldWriteToFileAndThenReadBackSameData() throws ConfigurationException, IOException {

        writeToFileAndThenReadBackSameData("xml");
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldErrorWhenAskedToReadMissingFile() throws ConfigurationException, IOException {

        getPersistence("target/xxx.xxx").load();
    }

    private void writeToFileAndThenReadBackSameData(String extension) throws ConfigurationException, IOException {

        String targetFilename = "target/ConfigurationRawDataPersistenceTest-raw-data." + extension;
        ConfigurationRawDataPersistence configurationRawDataPersistence = getPersistence(targetFilename);
        configurationRawDataPersistence.save(rawData);
        assertTrue(new File(targetFilename).exists());
        RawData loadedRawData = configurationRawDataPersistence.load();
        assertNotNull(loadedRawData);
    }

    private ConfigurationRawDataPersistence getPersistence(String targetFilename) throws ConfigurationException {

        new File(targetFilename).delete();
        String filename = mockConfiguration.getProperty("raw.data.store.filename");
        when(filename).thenReturn(targetFilename);
        return new ConfigurationRawDataPersistence(mockConfiguration);
    }
}
