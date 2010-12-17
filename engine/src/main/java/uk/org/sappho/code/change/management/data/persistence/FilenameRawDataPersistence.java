package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;

public class FilenameRawDataPersistence extends RawDataPersistence {

    private final String filename;

    @Inject
    public FilenameRawDataPersistence(String filename) {

        this.filename = filename;
    }

    public RawData load() throws IOException {

        Reader reader = new FileReader(filename);
        return load(reader);
    }

    public void save(RawData rawData) throws IOException {

        Writer writer = new FileWriter(filename);
        save(rawData, writer);
    }

    @Override
    public String description() {

        return filename;
    }
}
