package uk.org.sappho.code.change.management.app;

import java.io.IOException;

import uk.org.sappho.code.change.management.data.processing.RawDataProcessingException;
import uk.org.sappho.code.change.management.engine.Engine;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.ConfigurationException;

public class FakeEngine implements Engine {

    public void loadRawData() throws IOException, ConfigurationException {
    }

    public void processRawData() throws RawDataProcessingException, ConfigurationException {
    }

    public void refreshRawData() throws ConfigurationException, IssueManagementException {
    }

    public void saveRawData() throws IOException, ConfigurationException {
    }

    public void scanSCM() throws SCMException, ConfigurationException, IssueManagementException {
    }

    public void validateRawData() throws Exception {
    }
}
