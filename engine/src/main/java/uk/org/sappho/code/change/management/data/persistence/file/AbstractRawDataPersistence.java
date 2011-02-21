package uk.org.sappho.code.change.management.data.persistence.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.change.management.data.ChangedFile;
import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.Warnings;
import uk.org.sappho.code.change.management.data.persistence.RawDataPersistence;

public abstract class AbstractRawDataPersistence implements RawDataPersistence {

    protected static final Logger log = Logger.getLogger(RawDataPersistence.class);

    private final XStream xstream = new XStream();

    protected AbstractRawDataPersistence() {

        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class, Warnings.class,
                ChangedFile.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    protected RawData load(InputStream inputStream) throws IOException {

        log.info("Loading data from " + getDescription());
        RawData rawData = null;
        inputStream = new BufferedInputStream(inputStream);
        inputStream.mark(4096);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = null;
        try {
            zipEntry = zipInputStream.getNextEntry();
        } catch (Throwable t) {
        }
        if (zipEntry == null) {
            inputStream.reset();
            rawData = load(new InputStreamReader(inputStream));
        } else {
            boolean foundFile = false;
            while (zipEntry != null) {
                String zipFilename = zipEntry.getName();
                try {
                    rawData = loadFromZip(zipInputStream, zipFilename);
                    foundFile = true;
                    break;
                } catch (Throwable t) {
                    log.info("Data in " + zipFilename + " is unreadable", t);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            if (!foundFile) {
                throw new ZipException("Unable to find a data file in ZIP file in " + getDescription());
            }
        }
        return rawData;
    }

    private RawData loadFromZip(InputStream zipInputStream, String zipFilename) throws IOException {

        log.info("Loading data from " + zipFilename + " within ZIP file");
        return load(new InputStreamReader(zipInputStream));
    }

    private RawData load(Reader reader) throws IOException {

        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        log.info("Loaded data");
        return rawData;
    }

    protected void save(RawData rawData, OutputStream outputStream, String zipFilename)
            throws IOException {

        boolean compress = zipFilename != null && zipFilename.length() > 0;
        log.info("Saving data in " + (compress ? "compressed file " + zipFilename + " in " : "") + getDescription());
        outputStream = new BufferedOutputStream(outputStream);
        if (compress) {
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            ZipEntry zipEntry = new ZipEntry(zipFilename);
            zipEntry.setMethod(ZipEntry.DEFLATED);
            zipOutputStream.putNextEntry(zipEntry);
            outputStream = zipOutputStream;
        }
        Writer writer = new OutputStreamWriter(outputStream);
        xstream.toXML(rawData, writer);
        outputStream.close();
    }

    abstract public String getDescription();
}
