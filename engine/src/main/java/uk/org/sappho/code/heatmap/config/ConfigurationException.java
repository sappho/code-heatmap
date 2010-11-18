package uk.org.sappho.code.heatmap.config;

public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 7719403596012822053L;

    public ConfigurationException(String message) {

        super(message);
    }

    public ConfigurationException(String message, Throwable t) {

        super(message, t);
    }
}
