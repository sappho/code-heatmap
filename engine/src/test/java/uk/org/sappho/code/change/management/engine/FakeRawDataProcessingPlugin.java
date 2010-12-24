package uk.org.sappho.code.change.management.engine;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;

public class FakeRawDataProcessingPlugin implements RawDataProcessing {

    @Inject
    public FakeRawDataProcessingPlugin() {
    }

    public void run(RawData rawData) throws RawDataProcessingException {
    }
}
