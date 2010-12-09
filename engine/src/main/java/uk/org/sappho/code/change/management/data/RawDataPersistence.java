package uk.org.sappho.code.change.management.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.configuration.Configuration;

public class RawDataPersistence {

    private final XStream xstream = new XStream();
    private final String storeFilename;
    private static final Logger LOG = Logger.getLogger(RawDataPersistence.class);

    public RawDataPersistence(Configuration config) {

        storeFilename = config.getProperty("raw.data.store.filename", "heatmap-data.xml");
        for (Class clazz : new Class[] { RawData.class, IssueData.class, ChangeSet.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public RawData load() throws IOException, IssueManagementException {

        LOG.info("Loading data from " + storeFilename);
        Reader reader = new FileReader(storeFilename);
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public void save(RawData rawData) throws IOException {

        LOG.info("Saving data to " + storeFilename);
        Writer writer = new FileWriter(storeFilename);
        xstream.toXML(rawData, writer);
        writer.close();
    }
}
