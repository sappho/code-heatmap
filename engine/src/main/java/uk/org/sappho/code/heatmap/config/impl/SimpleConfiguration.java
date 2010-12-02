package uk.org.sappho.code.heatmap.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;

public class SimpleConfiguration implements Configuration {

    private final Properties properties = new Properties(System.getProperties());
    private static final Logger LOG = Logger.getLogger(SimpleConfiguration.class);

    @Inject
    public SimpleConfiguration() {

        LOG.info("Using plain properties file configuration plugin");
    }

    public void load(String filename) throws FileNotFoundException, IOException {

        LOG.info("Loading configuration from " + filename);
        properties.load(new FileReader(filename));
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
}
