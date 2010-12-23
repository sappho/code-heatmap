package uk.org.sappho.warnings.validation;

import java.util.List;
import java.util.Map;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class WarningListCheck extends AbstractAnnotationCheck<WarningListConstraint> {

    private static final long serialVersionUID = 5510376426614007473L;

    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        @SuppressWarnings("unchecked")
        Map<String, List<String>> warningLists = (Map<String, List<String>>) valueToValidate;
        if (warningLists == null)
            return false;
        for (String category : warningLists.keySet()) {
            if (category == null || category.length() == 0)
                return false;
        }
        return true;
    }
}
