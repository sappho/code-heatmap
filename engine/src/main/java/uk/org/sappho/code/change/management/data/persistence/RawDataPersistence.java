package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

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

        log.info("Loading data from " + description());
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        return rawData;
    }

    public void save(RawData rawData, Writer writer) throws IOException {

        log.info("Saving data to " + description());
        xstream.toXML(rawData, writer);
        writer.close();
    }

    abstract public String description();
}
