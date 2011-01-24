package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;

import uk.org.sappho.code.change.management.data.RawData;

public interface RawDataPersistence {

    public RawData load() throws IOException;

    public void save(RawData rawData) throws IOException;
}
