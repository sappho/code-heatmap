package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.warnings.SimpleWarningList;

public class RawDataPersistence {

    private final XStream xstream = new XStream();
    private String filename;
    private Reader dataReader;
    private static final Logger log = Logger.getLogger(RawDataPersistence.class);

    private RawDataPersistence() {

        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class, SimpleWarningList.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    public RawDataPersistence(String filename) throws FileNotFoundException {

        this();
        this.filename = filename;
        dataReader = new FileReader(filename);
    }

    public RawDataPersistence(Configuration config) throws ConfigurationException, FileNotFoundException {

        this();
        filename = config.getProperty("raw.data.store.filename");
        dataReader = new FileReader(filename);
    }

    public RawDataPersistence(Reader dataReader) {

        this();
        this.dataReader = dataReader;
    }

    public RawData load() throws IOException, IssueManagementException {

        log.info("Loading data from " + filename);
        Reader reader = new FileReader(filename);
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public RawData loadFromInputStream() {
        log.info("Loading data from inputStream");
        RawData rawData = (RawData) xstream.fromXML(dataReader);
        return rawData;
    }

    public void save(RawData rawData) throws IOException {

        log.info("Saving data to " + filename);
        Writer writer = new FileWriter(filename);
        xstream.toXML(rawData, writer);
        writer.close();
    }
}
