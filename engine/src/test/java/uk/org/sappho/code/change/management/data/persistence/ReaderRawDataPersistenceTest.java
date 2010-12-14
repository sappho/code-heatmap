package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;

public class ReaderRawDataPersistenceTest {

    @Test
    public void shouldReadInputStream() throws IOException, IssueManagementException {
        Reader testData = new InputStreamReader(getClass().getResourceAsStream("raw-data-11.01.xml"));
        ReaderRawDataPersistence persistence = new ReaderRawDataPersistence();

        RawData rawData = persistence.load(testData);

        assertNotNull(rawData);
    }

}
