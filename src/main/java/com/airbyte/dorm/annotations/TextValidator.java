package com.airbyte.dorm.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TextValidator implements ConstraintValidator<Text , String> {
    @Override
    public void initialize(Text constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches("[a-zA-Z\\- ]+") ;
    }
}
