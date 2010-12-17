package uk.org.sappho.code.change.management.engine;

import uk.org.sappho.code.change.management.data.RawData;

public interface Engine {

    public void run(RawData rawData) throws EngineException;
}
