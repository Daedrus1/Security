package mate.academy.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String first;
    private String second;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field f1 = value.getClass().getDeclaredField(first);
            Field f2 = value.getClass().getDeclaredField(second);
            f1.setAccessible(true);
            f2.setAccessible(true);
            Object v1 = f1.get(value);
            Object v2 = f2.get(value);
            return (v1 == null && v2 == null) || (v1 != null && v1.equals(v2));
        } catch (Exception e) {
            return false;
        }
    }
}


