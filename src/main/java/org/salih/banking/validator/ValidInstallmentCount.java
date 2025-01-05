package org.salih.banking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = InstallmentCountValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInstallmentCount {
    String message() default "Invalid installment count. Allowed values are 6, 9, 12, or 24.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}