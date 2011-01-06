package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;
import java.io.InputStream;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;

public class ReaderRawDataPersistence extends RawDataPersistence {

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
}
