package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import com.google.inject.Inject;

import uk.org.sappho.code.change.management.data.RawData;

public class FilenameRawDataPersistence extends RawDataPersistence {

    private final String filename;
    private String zipFilename;

    @Inject
    public FilenameRawDataPersistence(String filename) {

        this.filename = filename;
        zipFilename = null;
        if (filename.endsWith(".zip")) {
            zipFilename = filename.replaceFirst(".zip$", ".xml");
        }
    }

    public RawData load() throws IOException {

        Reader reader = new FileReader(filename);
        return load(reader);
    }

    public void save(RawData rawData) throws IOException {

        OutputStream outputStream = new FileOutputStream(filename);
        save(rawData, outputStream, zipFilename);
    }

    @Override
    public String getDescription() {

        return filename;
    }
}
