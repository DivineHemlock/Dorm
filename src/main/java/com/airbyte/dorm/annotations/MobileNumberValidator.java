package com.airbyte.dorm.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileNumberValidator implements ConstraintValidator<MobileNumber, String> {

    @Override
    public void initialize(MobileNumber contactNumber) {
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("[0-9]+")
                && (contactField.length() == 11);
    }
}
