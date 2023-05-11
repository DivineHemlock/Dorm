package com.airbyte.dorm.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberValidator implements ConstraintValidator<Number ,String>
{

    @Override
    public void initialize(Number constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return  s != null && s.matches("[0-9]+");
    }
}
