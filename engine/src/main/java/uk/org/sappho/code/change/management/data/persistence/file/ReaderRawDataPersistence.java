package uk.org.sappho.code.change.management.data.persistence.file;

import java.io.IOException;
import java.io.InputStream;

import uk.org.sappho.code.change.management.data.RawData;

public class ReaderRawDataPersistence extends AbstractRawDataPersistence {

    public ReaderRawDataPersistence() {

        super();
    }

    @Override
    public RawData load(InputStream inputStream) throws IOException {

        return super.load(inputStream);
    }

    @Override
    public String getDescription() {

        return "Reader stream";
    }

    public RawData load() throws IOException {

        throw new RuntimeException("ReaderRawDataPersistence will only load data from an InputStream");
    }

    public void save(RawData rawData) throws IOException {

        throw new RuntimeException("ReaderRawDataPersistence will not save data");
    }
}
