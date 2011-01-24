package uk.org.sappho.code.change.management.data.processing;

public class RawDataProcessingException extends Exception {

    private static final long serialVersionUID = 453243855034847435L;

    public RawDataProcessingException(String message) {

        super(message);
    }

    public RawDataProcessingException(String message, Throwable t) {

        super(message, t);
    }
}
