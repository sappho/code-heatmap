package uk.org.sappho.code.heatmap.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.ConfigException;

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
        }
    }

    public String getProperty(String name) throws ConfigException {

        String value = properties.getProperty(name);
        if (value == null) {
            throw new ConfigException("Configuration parameter " + name + " is missing");
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
