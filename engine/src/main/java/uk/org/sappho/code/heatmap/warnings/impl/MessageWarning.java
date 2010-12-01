package uk.org.sappho.code.heatmap.warnings.impl;

public class MessageWarning extends Warning {

    private final String message;

    public MessageWarning(String message) {

        this.message = message;
    }

    @Override
    public String getTypeName() {

        return "Warning";
    }

    @Override
    public String toString() {

        return message;
    }
}
