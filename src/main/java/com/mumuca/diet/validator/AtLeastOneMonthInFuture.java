package com.mumuca.diet.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AtLeastOneMonthInFutureValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneMonthInFuture {
    String message() default "The deadline must be at least one month in the future.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}