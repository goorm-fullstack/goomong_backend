package R.VD.goomong.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

    private EnumValue annotation;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString()) || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
