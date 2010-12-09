package uk.org.sappho.configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SimpleConfiguration implements Configuration {

    private final Properties properties = new Properties(System.getProperties());
    private Properties snapshotProperties = null;
    private static final Logger LOG = Logger.getLogger(SimpleConfiguration.class);

    @Inject
    public SimpleConfiguration() {

        LOG.info("Using plain properties file configuration plugin");
    }

    public String getProperty(String name) throws ConfigurationException {

        String value = properties.getProperty(name);
        if (value == null) {
            throw new ConfigurationException("Configuration parameter " + name + " is missing");
        }
        return value;
    }

    public String getProperty(String name, String defaultValue) {

        return properties.getProperty(name, defaultValue);
    }

    public List<String> getPropertyList(String name) throws ConfigurationException {

        List<String> list = getPropertyList(name, null);
        if (list == null) {
            throw new ConfigurationException("Configuration parameter " + name + ".1 is missing");
        }
        return list;
    }

    public List<String> getPropertyList(String name, List<String> defaultValue) {

        List<String> list = new Vector<String>();
        int index = 1;
        while (true) {
            String value = getProperty(name + "." + index++, null);
            if (value == null) {
                break;
            }
            list.add(value);
        }
        if (list.size() == 0) {
            list = defaultValue;
        }
        return list;
    }

    public Class<?> getPlugin(String name, String defaultPackage) throws ConfigurationException {

        String className = getProperty(name);
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            className = defaultPackage + "." + className;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e1) {
                throw new ConfigurationException("Unable to load plugin " + className
                        + " from configuration parameter " + name, e1);
            }
        }
        return clazz;
    }

    public void setProperty(String name, String value) {

        properties.setProperty(name, value);
    }

    public void load(String filename) throws ConfigurationException {

        LOG.info("Loading configuration from " + filename);
        try {
            Reader reader = new FileReader(filename);
            properties.load(reader);
            reader.close();
        } catch (IOException e) {
            throw new ConfigurationException("Unable to load configuration from " + filename, e);
        }
    }

    public void takeSnapshot() {

        snapshotProperties = new Properties();
        for (Object nameObj : properties.keySet()) {
            String name = (String) nameObj;
            String value = properties.getProperty(name);
            snapshotProperties.setProperty(name, value);
        }
    }

    public void saveChanged(String filenameKey) throws ConfigurationException {

        String filename = getProperty(filenameKey, null);
        if (filename != null) {
            if (snapshotProperties != null) {
                Properties changedProperties = new Properties();
                for (Object nameObj : properties.keySet()) {
                    String name = (String) nameObj;
                    String value = properties.getProperty(name);
                    String originalValue = snapshotProperties.getProperty(name);
                    if (originalValue == null || !originalValue.equals(value)) {
                        changedProperties.setProperty(name, value);
                    }
                }
                if (changedProperties.size() != 0) {
                    LOG.info("Saving changed configuration items to " + filename);
                    try {
                        Writer writer = new FileWriter(filename);
                        changedProperties.store(writer, null);
                        writer.close();
                    } catch (IOException e) {
                        throw new ConfigurationException("Unable to save changed configuration to " + filename, e);
                    }
                }
                snapshotProperties = null;
            } else {
                LOG.debug("Attempt to save property changes without taking a snapshot first");
            }
        } else {
            LOG.info("configuration property " + filenameKey + " not specified so not saving property updates");
        }
    }
}
