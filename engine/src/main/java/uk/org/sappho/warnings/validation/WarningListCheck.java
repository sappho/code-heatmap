package uk.org.sappho.warnings.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import uk.org.sappho.warnings.WarningList;

public class WarningListCheck extends AbstractAnnotationCheck<WarningListConstraint> {

    private static final long serialVersionUID = 5510376426614007473L;
    private static final String validityError = "Invalid warnings list";

    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        @SuppressWarnings("unchecked")
        Map<String, List<String>> warningLists = (Map<String, List<String>>) valueToValidate;
        if (warningLists == null) {
            ((WarningList) validatedObject).add(validityError, "Warnings list is missing");
            return false;
        }
        List<String> errors = new ArrayList<String>();
        for (String category : warningLists.keySet()) {
            if (category == null || category.length() == 0)
                errors.add("Warnings list contains a blank category");
            for (String warning : warningLists.get(category)) {
                if (warning == null || warning.length() == 0) {
                    errors.add("Warnings list category \"" + category + "\" contains a blank warning");
                    break;
                }
            }
        }
        for (String error : errors) {
            ((WarningList) validatedObject).add(validityError, error);
        }
        return errors.size() == 0;
    }
}
