package org.salih.banking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private long loanId;
    private BigDecimal amount;
}
