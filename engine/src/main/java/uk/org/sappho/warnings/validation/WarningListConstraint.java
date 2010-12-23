package uk.org.sappho.warnings.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.configuration.annotation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(checkWith = WarningListCheck.class)
public @interface WarningListConstraint {

    String message() default "Warnings list is missing or contains a blank category";
}
