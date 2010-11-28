package uk.org.sappho.code.heatmap.config;

public interface Configuration {

    public String getProperty(String name) throws ConfigurationException;

    public String getProperty(String name, String defaultValue);

    public void setProperty(String name, String value);

    public Class<?> getPlugin(String name, String defaultPackage) throws ConfigurationException;
}
