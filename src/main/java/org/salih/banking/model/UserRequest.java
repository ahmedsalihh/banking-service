package org.salih.banking.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRequest {
    @NotNull(message = "firstname is required")
    private String firstname;

    @NotNull(message = "lastname is required")
    private String lastname;

    @NotNull(message = "creditLimit is required")
    @DecimalMin(value = "0.0", message = "creditLimit must be greater than or equal to 0")
    private BigDecimal creditLimit;

    private BigDecimal usedCreditLimit = BigDecimal.ZERO;
}
