package org.salih.banking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResult {
    private int installmentsPaid;
    private BigDecimal totalAmountPaid;
    private boolean loanPaidCompletely;
}
