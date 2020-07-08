package residentevil.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = CapitalsListValidationImpl.class)
@ReportAsSingleViolation
public @interface CapitalsListValidation {

    String message() default "Choose at least one of the provided capitals!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
