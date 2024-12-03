package com.mumuca.diet.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AtLeastOneMonthInFutureValidator
        implements ConstraintValidator<AtLeastOneMonthInFuture, LocalDate> {

    @Override
    public boolean isValid(LocalDate deadline, ConstraintValidatorContext context) {
        if (deadline == null) {
            return true;
        }
        LocalDate oneMonthFromNow = LocalDate.now().plusMonths(1);
        return deadline.isAfter(oneMonthFromNow) || deadline.isEqual(oneMonthFromNow);
    }
}
