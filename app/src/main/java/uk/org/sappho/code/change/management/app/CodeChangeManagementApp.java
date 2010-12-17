package uk.org.sappho.code.change.management.app;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.persistence.ConfigurationRawDataPersistence;
import uk.org.sappho.code.change.management.engine.Engine;
import uk.org.sappho.code.change.management.engine.EngineException;
import uk.org.sappho.code.change.management.engine.EngineModule;
import uk.org.sappho.code.change.management.scm.SCM;
import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.configuration.SimpleConfiguration;

public class CodeChangeManagementApp {

    private static final Logger log = Logger.getLogger(CodeChangeManagementApp.class);

    protected void run(Configuration config) throws ConfigurationException, IOException, SCMException, EngineException {

        EngineModule engineModule = new EngineModule(config);
        engineModule.init();
        RawData rawData = new RawData();
        ConfigurationRawDataPersistence rawDataPersistence = new ConfigurationRawDataPersistence(config);
        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            log.info("Running " + action);
            if (action.equalsIgnoreCase("load")) {
                rawData = rawDataPersistence.load();
            } else if (action.equalsIgnoreCase("save")) {
                rawDataPersistence.save(rawData);
            } else if (action.equalsIgnoreCase("scan")) {
                SCM scm = engineModule.getSCMPlugin();
                scm.scan(rawData);
                engineModule.refreshRawData(rawData);
            } else if (action.equalsIgnoreCase("refresh")) {
                engineModule.refreshRawData(rawData);
            } else if (action.equalsIgnoreCase("process")) {
                Engine rawDataProcessingEngine = engineModule.getRawDataProcessingPlugin();
                rawDataProcessingEngine.run(rawData);
            } else {
                throw new ConfigurationException("Action " + action + " is unrecognised");
            }
        }
    }

    public static void main(String[] args) {

        try {
            Configuration config = new SimpleConfiguration();
            for (String configFilename : args) {
                config.load(configFilename);
            }
            new CodeChangeManagementApp().run(config);
            log.info("Everything done");
        } catch (Throwable t) {
            log.error("Application error", t);
        }
    }
}
