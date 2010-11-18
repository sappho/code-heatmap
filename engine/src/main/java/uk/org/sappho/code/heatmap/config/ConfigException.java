package uk.org.sappho.code.heatmap.config;

public class ConfigException extends Exception {

    private static final long serialVersionUID = 7719403596012822053L;

    public ConfigException(String message) {

        super(message);
    }

    public ConfigException(String message, Throwable t) {

        super(message, t);
    }
}
