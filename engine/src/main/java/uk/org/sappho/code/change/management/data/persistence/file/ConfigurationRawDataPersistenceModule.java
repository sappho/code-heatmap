package uk.org.sappho.code.change.management.data.persistence.file;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.change.management.data.persistence.RawDataPersistence;

public class ConfigurationRawDataPersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RawDataPersistence.class).to(ConfigurationRawDataPersistence.class);
    }
}
