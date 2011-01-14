package uk.org.sappho.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class WarningsCheck extends AbstractAnnotationCheck<WarningsConstraint> {

    private static final long serialVersionUID = 8522526115519114889L;

    @SuppressWarnings("unchecked")
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        List<String> errors = new ArrayList<String>();
        Map<String, List<String>> warningLists = null;
        Set<String> categories = null;
        try {
            warningLists = (Map<String, List<String>>) valueToValidate;
            categories = warningLists.keySet();
        } catch (Throwable t) {
            // catches cast exceptions leaving keys as null
        }
        if (categories == null)
            errors.add("null or not a list of warnings");
        else
            for (String category : categories) {
                if (category == null)
                    errors.add("null category");
                else if (category.length() == 0)
                    errors.add("blank category");
                String errorLabel = " for category \"" + category + "\"";
                List<String> warnings = warningLists.get(category);
                if (warnings == null)
                    errors.add("null warnings list" + errorLabel);
                else
                    for (String warning : warnings)
                        if (warning == null)
                            errors.add("null warning" + errorLabel);
                        else if (warning.length() == 0)
                            errors.add("blank warning" + errorLabel);
            }
        if (errors.size() != 0) {
            String error = "";
            if (context instanceof FieldContext) {
                FieldContext fieldContext = (FieldContext) context;
                error = fieldContext.getField().getDeclaringClass().getName() + "." + fieldContext.getField().getName()
                        + " ";
            }
            setMessage(error + errors);
        }
        return errors.size() == 0;
    }
}
