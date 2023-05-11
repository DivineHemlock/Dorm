package com.airbyte.dorm.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidator.class)
@Target({ElementType.METHOD , ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Text
{
    String message() default "Invalid entry";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
