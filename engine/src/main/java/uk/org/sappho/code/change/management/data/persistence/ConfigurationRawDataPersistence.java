package uk.org.sappho.code.change.management.data.persistence;

import com.google.inject.Inject;

import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

public class ConfigurationRawDataPersistence extends FilenameRawDataPersistence {

    @Inject
    public ConfigurationRawDataPersistence(Configuration config) throws ConfigurationException {

        super(config.getProperty("raw.data.store.filename"));
    }
}
