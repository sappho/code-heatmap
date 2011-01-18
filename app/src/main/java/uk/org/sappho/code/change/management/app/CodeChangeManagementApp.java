package uk.org.sappho.code.change.management.app;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.change.management.engine.EngineModule;
import uk.org.sappho.code.heatmap.basic.BasicHeatMapModule;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.configuration.SimpleConfiguration;

public class CodeChangeManagementApp {

    private static final Logger log = Logger.getLogger(CodeChangeManagementApp.class);

    private void run(String[] args) throws Throwable {

        Configuration config = new SimpleConfiguration();
        for (String configFilename : args)
            config.load(configFilename);
        EngineModule engineModule = new EngineModule(config);
        BasicHeatMapModule basicHeatMapModule = new BasicHeatMapModule(config);
        Injector injector = Guice.createInjector(engineModule, basicHeatMapModule);
        engineModule.setInjector(injector);
        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            log.info("Running " + action);
            if (action.equalsIgnoreCase("load"))
                engineModule.loadRawData();
            else if (action.equalsIgnoreCase("validate"))
                engineModule.validateRawData();
            else if (action.equalsIgnoreCase("save"))
                engineModule.saveRawData();
            else if (action.equalsIgnoreCase("scan"))
                engineModule.scanSCM();
            else if (action.equalsIgnoreCase("refresh"))
                engineModule.refreshRawData();
            else if (action.equalsIgnoreCase("process"))
                engineModule.processRawData();
            else
                throw new ConfigurationException("Action " + action + " is unrecognised");
            memoryStats();
        }
    }

    private static void memoryStats() {

        log.info("Total memory: " + Runtime.getRuntime().totalMemory() / 1024 + "K  Free memory: "
                + Runtime.getRuntime().freeMemory() / 1024 + "K");
    }

    public static void main(String[] args) {

        try {
            new CodeChangeManagementApp().run(args);
            log.info("Everything done");
        } catch (Throwable t) {
            memoryStats();
            log.error("Error!", t);
        }
    }
}
