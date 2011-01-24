package uk.org.sappho.code.change.management.engine;

import java.io.IOException;

import uk.org.sappho.code.change.management.data.processing.RawDataProcessingException;
import uk.org.sappho.code.change.management.issues.IssueManagementException;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.ConfigurationException;

public interface Engine {

    public void loadRawData() throws IOException, ConfigurationException;

    public void saveRawData() throws IOException, ConfigurationException;

    public void validateRawData() throws Exception;

    public void scanSCM() throws SCMException, ConfigurationException, IssueManagementException;

    public void processRawData() throws RawDataProcessingException, ConfigurationException;

    public void refreshRawData() throws ConfigurationException, IssueManagementException;
}
