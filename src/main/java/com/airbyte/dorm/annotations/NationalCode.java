package com.airbyte.dorm.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NationalCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NationalCode {
    String message() default "Invalid national code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
