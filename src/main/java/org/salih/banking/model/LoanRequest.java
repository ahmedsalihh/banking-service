package org.salih.banking.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.salih.banking.validator.ValidInstallmentCount;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", message = "amount must be greater than or equal to 0")
    private BigDecimal amount;

    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must not exceed 0.5")
    private float interestRate;

    @ValidInstallmentCount
    private int installmentCount;
}
