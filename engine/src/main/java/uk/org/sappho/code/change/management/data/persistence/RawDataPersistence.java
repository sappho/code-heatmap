package uk.org.sappho.code.change.management.data.persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.warnings.SimpleWarningList;

import com.thoughtworks.xstream.XStream;

public abstract class RawDataPersistence {

    protected static final Logger log = Logger.getLogger(RawDataPersistence.class);

    private final XStream xstream = new XStream();

    public RawDataPersistence() {

        for (Class<?> clazz : new Class[] { RawData.class, IssueData.class, RevisionData.class, SimpleWarningList.class }) {
            xstream.alias(clazz.getSimpleName(), clazz);
        }
    }

    public RawData load(Reader reader) throws IOException {

        log.info("Loading data");
        RawData rawData = (RawData) xstream.fromXML(reader);
        reader.close();
        log.info("Loaded data");
        return rawData;
    }

    public void save(RawData rawData, Writer writer) throws IOException {

        log.info("Writing data");
        xstream.toXML(rawData, writer);
        writer.close();
    }
}
