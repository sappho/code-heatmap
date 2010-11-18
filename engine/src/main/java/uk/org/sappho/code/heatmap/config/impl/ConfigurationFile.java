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
    public ConfigurationFile() throws FileNotFoundException, IOException {

        LOG.info("Using plain properties file configuration plugin");
        String filename = System.getProperty("config.filename");
        if (filename != null) {
            LOG.info("Loading configuration from " + filename);
            properties.load(new FileReader(filename));
        } else {
            LOG.info("Because config.filename system property does not exist only system properties will be used");
        }
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
}
