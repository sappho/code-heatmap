package uk.org.sappho.code.change.management.data.persistence;

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
import uk.org.sappho.warnings.SimpleWarningList;

public abstract class RawDataPersistence {

    private static final Logger LOG = Logger.getLogger(RawDataPersistence.class);

    private final XStream xstream = new XStream();
    private String filename;

    public RawData load(Reader reader) throws IOException, IssueManagementException {
        LOG.info("Loading data");
        setupXStream();
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public void save(RawData rawData) throws IOException {
        LOG.info("Saving data to " + filename);
        setupXStream();
        Writer writer = new FileWriter(filename);
        xstream.toXML(rawData, writer);
        writer.close();
    }

    private void setupXStream() {
        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class, SimpleWarningList.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }
}
