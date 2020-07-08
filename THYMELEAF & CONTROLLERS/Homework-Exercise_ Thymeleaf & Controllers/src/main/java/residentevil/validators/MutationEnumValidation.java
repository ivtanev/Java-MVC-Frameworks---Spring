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
@Constraint(validatedBy = MutationEnumValidationImpl.class)
@ReportAsSingleViolation
public @interface MutationEnumValidation {

    Class<? extends Enum<?>> enumClazz();

    String message() default "Invalid Mutation. Choose some of the given options";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
