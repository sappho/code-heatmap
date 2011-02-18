package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.persistence.file.ReaderRawDataPersistence;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

public abstract class BaseDataAnalysis<A extends Action<R>, R extends Result> implements ActionHandler<A, R> {

    private static final Logger LOG = Logger.getLogger(BaseDataAnalysis.class);

    private final ReaderRawDataPersistence rawDataPersistence;

    private RawData rawData;

    public BaseDataAnalysis(ReaderRawDataPersistence rawDataPersistence) {
        this.rawDataPersistence = rawDataPersistence;
    }

    protected RawData getRawData() {

        if (rawData != null) {
            return rawData;
        }

        File dataFile = new File("WEB-INF/data/raw-data-all-frame-2011-02-17.zip");
        try {
            if (dataFile.exists()) {
                InputStream inputStream = getDataFileInputStream(dataFile);
                rawData = rawDataPersistence.load(inputStream);
                return rawData;
            } else {
                LOG.error("Couldn't find data file: " + dataFile.getCanonicalPath());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private InputStream getDataFileInputStream(File dataFile) throws IOException, ZipException {
        LOG.info("Fetching data from: " + dataFile.getCanonicalFile());
        return new FileInputStream(dataFile);
    }

}