package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;

import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import uk.org.sappho.code.change.management.data.RawData;

public class RawDataPersistenceTest {

    @Test
    public void shouldReadInputStream() {
        Reader testData = new InputStreamReader(getClass().getResourceAsStream("raw-data-11.01.xml"));
        RawDataPersistence persistence = new RawDataPersistence(testData);

        RawData rawData = persistence.loadFromInputStream();

        assertNotNull(rawData);
    }

}
