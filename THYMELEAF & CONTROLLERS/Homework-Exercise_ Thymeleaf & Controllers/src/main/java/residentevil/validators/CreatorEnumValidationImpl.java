package residentevil.validators;

import residentevil.domain.entities.Creator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreatorEnumValidationImpl implements ConstraintValidator<CreatorEnumValidation, Creator> {
    @Override
    public boolean isValid(Creator value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return true;
    }
}
