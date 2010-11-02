package uk.org.sappho.code.heatmap.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static final Config config = new Config();
    private final Properties properties = new Properties(System.getProperties());

    public static final Config getConfig() {

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

    public void load(String filename) throws FileNotFoundException, IOException {

        properties.load(new FileReader(filename));
    }
}
