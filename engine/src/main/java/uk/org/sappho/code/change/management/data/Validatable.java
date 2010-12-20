package uk.org.sappho.code.change.management.data;

public interface Validatable {

    public void checkValidity() throws ValidationException;
}
