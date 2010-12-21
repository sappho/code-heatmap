package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RawDataTest;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationRawDataPersistenceTest {

    @Mock
    private Configuration mockConfiguration;

    private RawData rawData;

    @Before
    public void setupRawData() {

        rawData = RawDataTest.getFakeRawData();
        RawDataTest.reWireFakeRawData(rawData);
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
