package uk.org.sappho.code.change.management.data.persistence;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.warnings.SimpleWarningList;

public abstract class RawDataPersistence {

    protected static final Logger log = Logger.getLogger(RawDataPersistence.class);

    private final XStream xstream = new XStream();

    public RawDataPersistence() {

        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class, SimpleWarningList.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    public RawData load(Reader reader) throws IOException {

        log.info("Loading data from " + getDescription());
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public void save(RawData rawData, OutputStream outputStream, String zipFilename)
            throws IOException {

        boolean compress = zipFilename != null && zipFilename.length() > 0;
        log.info("Saving data to " + getDescription() + (compress ? " in compressed zip file " + zipFilename : ""));
        if (compress) {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
            ZipEntry zipEntry = new ZipEntry(zipFilename);
            zipOutputStream.putNextEntry(zipEntry);
            outputStream = zipOutputStream;
        }
        Writer writer = new OutputStreamWriter(outputStream);
        xstream.toXML(rawData, writer);
        outputStream.close();
    }

    abstract public String getDescription();
}
