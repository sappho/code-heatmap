package uk.org.sappho.validation;

import java.util.List;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class StringArrayCheck extends AbstractAnnotationCheck<StringArrayConstraint> {

    private static final long serialVersionUID = 5510376426614007473L;

    @SuppressWarnings("unchecked")
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        String error = null;
        List<String> array = null;
        try {
            array = (List<String>) valueToValidate;
        } catch (Throwable t) {
            // catch null or wrong type
        }
        if (array == null)
            error = "is missing or invalid";
        else
            for (String item : array) {
                boolean invalid = true;
                try {
                    invalid = item.length() == 0;
                } catch (Throwable t) {
                    // catch null or wrong type
                }
                if (invalid)
                    error = "contains null or blank string";
            }
        if (error != null) {
            if (context instanceof FieldContext) {
                FieldContext fieldContext = (FieldContext) context;
                error = fieldContext.getField().getDeclaringClass().getName() + "." + fieldContext.getField().getName()
                        + " " + error;
            }
            setMessage(error);
        }
        return error == null;
    }
}
