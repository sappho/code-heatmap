package uk.org.sappho.code.change.management.engine;

import uk.org.sappho.code.change.management.data.RawData;

public interface RawDataProcessing {

    public void run(RawData rawData) throws RawDataProcessingException;
}
