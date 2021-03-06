package uk.org.sappho.code.change.management.data.persistence.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.org.sappho.code.change.management.data.RawData;

public class FilenameRawDataPersistence extends AbstractRawDataPersistence {

    private final String filename;

    public FilenameRawDataPersistence(String filename) {

        super();
        this.filename = filename;
    }

    public RawData load() throws IOException {

        return load(new FileInputStream(filename));
    }

    public void save(RawData rawData) throws IOException {

        String zipFilename = null;
        if (filename.endsWith(".zip"))
            zipFilename = new File(filename.replaceFirst(".zip$", ".xml")).getName();
        save(rawData, new FileOutputStream(filename), zipFilename);
    }

    @Override
    public String getDescription() {

        return filename;
    }
}
