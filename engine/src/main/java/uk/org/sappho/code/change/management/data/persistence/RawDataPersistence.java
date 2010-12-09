package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

public class RawDataPersistence {

    private final XStream xstream = new XStream();
    private final String filename;
    private static final Logger LOG = Logger.getLogger(RawDataPersistence.class);

    public RawDataPersistence(String filename) {

        this.filename = filename;
        init();
    }

    public RawDataPersistence(Configuration config) throws ConfigurationException {

        filename = config.getProperty("raw.data.store.filename");
        init();
    }

    private void init() {

        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    public RawData load() throws IOException, IssueManagementException {

        LOG.info("Loading data from " + filename);
        Reader reader = new FileReader(filename);
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public void save(RawData rawData) throws IOException {

        LOG.info("Saving data to " + filename);
        Writer writer = new FileWriter(filename);
        xstream.toXML(rawData, writer);
        writer.close();
    }
}
