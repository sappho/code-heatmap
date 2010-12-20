package uk.org.sappho.code.change.management.data;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 1805177554093770162L;

    public ValidationException(String message) {

        super(message);
    }

    public ValidationException(String message, Throwable t) {

        super(message, t);
    }
}
