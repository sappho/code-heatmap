package uk.org.sappho.code.change.management.data.processing;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.configuration.ConfigurationException;

public interface RawDataProcessing {

    public void run(RawData rawData) throws RawDataProcessingException, ConfigurationException;
}
