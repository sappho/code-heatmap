package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;
import java.io.Reader;

import uk.org.sappho.code.change.management.data.RawData;

public class ReaderRawDataPersistence extends RawDataPersistence {

    public ReaderRawDataPersistence() {

        super();
    }

    @Override
    public RawData load(Reader reader) throws IOException {

        return super.load(reader);
    }

    @Override
    public String getDescription() {

        return "Reader stream";
    }
}
