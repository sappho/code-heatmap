package uk.org.sappho.code.heatmap.config;

import java.util.List;

public interface Configuration {

    public String getProperty(String name) throws ConfigurationException;

    public String getProperty(String name, String defaultValue);

    public List<String> getPropertyList(String name) throws ConfigurationException;

    public List<String> getPropertyList(String name, List<String> defaultValue);

    public Class<?> getPlugin(String name, String defaultPackage) throws ConfigurationException;
}
