package com.airbyte.dorm.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TelephoneNumberValidator implements ConstraintValidator<TelephoneNumber, String> {

    @Override
    public void initialize(TelephoneNumber nationalCode) {
    }

    @Override
    public boolean isValid(String nationalCode, ConstraintValidatorContext cxt) {
        return nationalCode != null && nationalCode.matches("[0-9]+");
    }
}
