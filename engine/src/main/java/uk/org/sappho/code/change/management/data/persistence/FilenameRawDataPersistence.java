package uk.org.sappho.code.change.management.data.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.issues.IssueManagementException;

public class FilenameRawDataPersistence extends RawDataPersistence {

    public RawData load(String filename) {
        try {
            return super.load(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IssueManagementException e) {
            throw new RuntimeException(e);
        }
    }

}
