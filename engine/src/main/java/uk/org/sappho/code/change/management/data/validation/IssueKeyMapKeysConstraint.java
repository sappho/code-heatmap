package uk.org.sappho.code.change.management.data.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.configuration.annotation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
@Constraint(checkWith = IssueKeyMapCheck.class)
public @interface IssueKeyMapKeysConstraint {

    String message() default "Warnings list is missing or contains a blank category";
}
