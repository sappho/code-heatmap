package uk.org.sappho.code.heatmap.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import uk.org.sappho.code.heatmap.config.Config;
import uk.org.sappho.code.heatmap.config.ConfigException;

public class ConfigFile implements Config {

    public static final ConfigFile config = new ConfigFile();
    private final Properties properties = new Properties(System.getProperties());

    public static final ConfigFile getConfig() {

        return config;
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

    public void load(String filename) throws ConfigException {

        try {
            properties.load(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new ConfigException("Config file not found", e);
        } catch (IOException e) {
            throw new ConfigException("Config file could not be read", e);
        }
    }
}
