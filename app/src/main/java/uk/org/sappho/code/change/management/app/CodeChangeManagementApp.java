package uk.org.sappho.code.change.management.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Module;

import uk.org.sappho.code.change.management.engine.Engine;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.configuration.SimpleConfiguration;
import uk.org.sappho.configuration.SimpleConfigurationModule;

public class CodeChangeManagementApp {

    private static final Logger log = Logger.getLogger(CodeChangeManagementApp.class);

    protected void run(Configuration config) throws Throwable {

        List<Module> modules = new ArrayList<Module>();
        modules.add(new SimpleConfigurationModule(config));
        List<String> moduleNames = config.getPropertyList("module");
        for (String moduleName : moduleNames)
            modules.add(config.getGuiceModule(moduleName));

        Engine engine = Guice.createInjector(modules).getInstance(Engine.class);
        List<String> actions = config.getPropertyList("app.run.action");
        for (String action : actions) {
            log.info("Running " + action);
            if (action.equalsIgnoreCase("load"))
                engine.loadRawData();
            else if (action.equalsIgnoreCase("validate"))
                engine.validateRawData();
            else if (action.equalsIgnoreCase("save"))
                engine.saveRawData();
            else if (action.equalsIgnoreCase("scan"))
                engine.scanSCM();
            else if (action.equalsIgnoreCase("refresh"))
                engine.refreshRawData();
            else if (action.equalsIgnoreCase("process"))
                engine.processRawData();
            else
                throw new ConfigurationException("Action " + action + " is unrecognised");
            memoryStats();
        }
    }

    private void run(String[] args) throws Throwable {

        Configuration config = new SimpleConfiguration();
        for (String configFilename : args)
            config.load(configFilename);
        run(config);
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
