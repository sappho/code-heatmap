package uk.org.sappho.code.heatmap.config;


public interface Config {

    public String getProperty(String name) throws ConfigException;

    public String getProperty(String name, String defaultValue);

    public void setProperty(String name, String value);

    public void load(String filename) throws ConfigException;
}
