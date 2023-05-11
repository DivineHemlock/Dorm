package com.airbyte.dorm.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NationalCodeValidator  implements ConstraintValidator<NationalCode, String> {

    @Override
    public void initialize(NationalCode nationalCode) {
    }

    @Override
    public boolean isValid(String nationalCode, ConstraintValidatorContext cxt) {
        return nationalCode != null && nationalCode.matches("[0-9]+")
                && (nationalCode.length() == 10);
    }
}
