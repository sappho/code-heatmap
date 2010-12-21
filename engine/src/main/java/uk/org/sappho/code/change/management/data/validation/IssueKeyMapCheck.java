package uk.org.sappho.code.change.management.data.validation;

import java.util.Collection;
import java.util.Map;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import uk.org.sappho.code.change.management.data.RawData;

public class IssueKeyMapCheck extends AbstractAnnotationCheck<IssueKeyMapConstraint> {

    private static final long serialVersionUID = 2875771845270320136L;
    private static final String validityError = "Invalid issue mapping";

    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        RawData rawData = (RawData) validatedObject;
        @SuppressWarnings("unchecked")
        Map<String, String> issueKeyToIssueKeyMap = (Map<String, String>) valueToValidate;
        if (issueKeyToIssueKeyMap == null) {
            rawData.getWarnings().add(validityError, "Issue mappings are missing");
            return false;
        }
        boolean validKeys = areIssueKeysValid(rawData, issueKeyToIssueKeyMap.keySet());
        boolean validValues = areIssueKeysValid(rawData, issueKeyToIssueKeyMap.values());
        return validKeys && validValues;
    }

    private boolean areIssueKeysValid(RawData rawData, Collection<String> issueKeys) {

        boolean valid = true;
        for (String issueKey : issueKeys) {
            if (rawData.getIssueData(issueKey) == null) {
                rawData.getWarnings().add(validityError, "Issue \"" + issueKey + "\" does not point to any issue data");
                valid = false;
            }
        }
        return valid;
    }
}
