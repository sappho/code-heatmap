package uk.org.sappho.code.heatmap.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigurationException;

@Singleton
public class ConfigurationFile implements Configuration {

    private final Properties properties = new Properties(System.getProperties());
    private static final Logger LOG = Logger.getLogger(ConfigurationFile.class);

    @Inject
    public ConfigurationFile() {

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

    public void setProperty(String name, String value) {

        properties.setProperty(name, value);
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
