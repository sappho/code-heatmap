package uk.org.sappho.warnings.simple;

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
