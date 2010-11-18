package uk.org.sappho.code.heatmap.issue;

public class IssueException extends Exception {

    private static final long serialVersionUID = 6114194871519821338L;

    public IssueException(String message, Throwable t) {

        super(message, t);
    }
}
