package uk.org.sappho.code.change.management.data.persistence;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;

public class RawDataPersistenceTest {

	@Test
	public void shouldReadInputStream() throws IOException, IssueManagementException {
		InputStream testData = getClass().getResourceAsStream("raw-data-11.01.xml");
		RawDataPersistence persistence = new RawDataPersistence(testData);

		RawData rawData = persistence.loadFromInputStream();

		assertNotNull(rawData);
	}

}
