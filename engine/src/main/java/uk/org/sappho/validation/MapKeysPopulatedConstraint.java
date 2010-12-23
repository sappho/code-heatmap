package uk.org.sappho.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.configuration.annotation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
@Constraint(checkWith = MapKeysPopulatedCheck.class)
public @interface MapKeysPopulatedConstraint {
}
