package org.salih.banking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class InstallmentCountValidator implements ConstraintValidator<ValidInstallmentCount, Integer> {

    private static final Set<Integer> VALID_VALUES = Set.of(6, 9, 12, 24);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && VALID_VALUES.contains(value);
    }
}