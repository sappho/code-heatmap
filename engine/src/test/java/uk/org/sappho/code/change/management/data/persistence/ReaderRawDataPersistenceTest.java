package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.junit.Test;

import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;

public class ReaderRawDataPersistenceTest {

    @Test(expected = ZipException.class)
    public void shouldReadEmptyZipFile() throws IOException {

        InputStream testData = getClass().getResourceAsStream("nothing.zip");
        ReaderRawDataPersistence persistence = new ReaderRawDataPersistence();
        persistence.load(testData);
    }
}
