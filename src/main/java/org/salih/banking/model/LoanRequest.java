package org.salih.banking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private Long userId;
    private BigDecimal amount;
    private float interestRate;
    private int installmentCount;
}
