package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

public class ConfigurationRawDataPersistence extends RawDataPersistence {

    public RawData load(Configuration configuration) {

        try {
            return super.load(new FileReader(configuration.getProperty("raw.data.store.filename")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IssueManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
