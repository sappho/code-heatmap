package uk.org.sappho.code.change.management.issues;

public class IssueManagementException extends Exception {

    private static final long serialVersionUID = 6114194871519821338L;

    public IssueManagementException(String message) {

        super(message);
    }

    public IssueManagementException(String message, Throwable t) {

        super(message, t);
    }
}
