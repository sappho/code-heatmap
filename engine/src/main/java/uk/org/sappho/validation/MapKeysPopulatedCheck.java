package uk.org.sappho.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class MapKeysPopulatedCheck extends AbstractAnnotationCheck<MapKeysPopulatedConstraint> {

    private static final long serialVersionUID = 5510376426614007473L;

    @SuppressWarnings("unchecked")
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        Map<String, Object> map = null;
        Set<String> keys = null;
        try {
            map = (Map<String, Object>) valueToValidate;
            keys = map.keySet();
        } catch (Throwable t) {
            // catches cast exceptions leaving keys as null
        }
        String error = null;
        if (keys == null) {
            error = "is missing, invalid or does not cast to Map<String, Object>";
        } else {
            Map<String, Object> invalidMapParts = new HashMap<String, Object>();
            for (String key : keys) {
                if (key == null || key.length() == 0) {
                    Object object = "<invalid>";
                    try {
                        object = map.get(key);
                    } catch (Throwable t) {
                        // leave the value as invalid
                    }
                    invalidMapParts.put(key, object);
                }
            }
            if (invalidMapParts.keySet().size() != 0) {
                error = "contains invalid keys: " + invalidMapParts;
            }
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