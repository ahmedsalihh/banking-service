package org.salih.banking.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull(message = "loanId is required")
    private long loanId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", message = "amount must be greater than or equal to 0")
    private BigDecimal amount;
}
