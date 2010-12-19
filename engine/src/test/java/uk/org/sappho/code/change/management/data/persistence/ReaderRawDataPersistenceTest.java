package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.junit.Test;

import uk.org.sappho.code.change.management.data.RawData;

public class ReaderRawDataPersistenceTest {

    @Test
    public void shouldReadInputStream() throws IOException {

        readInputStream("xml");
    }

    @Test
    public void shouldReadZipInputStream() throws IOException {

        readInputStream("zip");
    }

    @Test(expected = ZipException.class)
    public void shouldReadEmptyZipFile() throws IOException {

        InputStream testData = getClass().getResourceAsStream("nothing.zip");
        ReaderRawDataPersistence persistence = new ReaderRawDataPersistence();
        persistence.load(testData);
    }

    private void readInputStream(String extension) throws IOException {

        InputStream testData = getClass().getResourceAsStream("raw-data-11.01." + extension);
        ReaderRawDataPersistence persistence = new ReaderRawDataPersistence();
        RawData rawData = persistence.load(testData);
        assertNotNull(rawData);
    }
}
