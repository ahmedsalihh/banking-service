package org.salih.banking.converter;

import org.salih.banking.entitiy.Installment;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.entitiy.User;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.exception.NotEnoughCreditLimitException;
import org.salih.banking.model.PaymentResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanConverter {
    public BigDecimal calculateTotalAmount(BigDecimal amount, float interestRate) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (interestRate < 0.1f || interestRate > 0.5f) {
            throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5.");
        }

        BigDecimal rate = BigDecimal.valueOf(interestRate);
        BigDecimal multiplier = BigDecimal.ONE.add(rate);
        return amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean checkUserCredibility(BigDecimal creditLimit, BigDecimal currentUsedCreditLimit, BigDecimal loanAmount) {
        if (creditLimit == null || currentUsedCreditLimit == null || loanAmount == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }

        BigDecimal totalUsedCredit = currentUsedCreditLimit.add(loanAmount);
        return totalUsedCredit.compareTo(creditLimit) <= 0;
    }

    public BigDecimal calculateNewUsedCreditLimit(BigDecimal currentUsedCreditLimit, BigDecimal loanAmount) {
        if (currentUsedCreditLimit == null || loanAmount == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }
        return currentUsedCreditLimit.add(loanAmount);
    }

    public void validateCreditLimit(User user, BigDecimal loanAmount) throws NotEnoughCreditLimitException {
        boolean isCreditLimitAvailable = checkUserCredibility(user.getCreditLimit(), user.getUsedCreditLimit(), loanAmount);
        if (!isCreditLimitAvailable) {
            throw new NotEnoughCreditLimitException("User doesn't have enough credit limit");
        }
    }

    public List<Installment> getPayableInstallments(Loan loan) {

        return loan.getInstallments().stream()
                .filter(installment -> installment.getStatus() == StatusEnum.CREATED)
                .filter(installment -> !installment.getDueDate().isAfter(LocalDate.now().plusMonths(3)))
                .sorted(Comparator.comparing(Installment::getDueDate))
                .collect(Collectors.toList());
    }

    public PaymentResult preparePaymentResult(int installmentsPaid, BigDecimal totalAmountPaid, boolean loanPaidCompletely) {
        PaymentResult result = new PaymentResult();
        result.setInstallmentsPaid(installmentsPaid);
        result.setTotalAmountPaid(totalAmountPaid);
        result.setLoanPaidCompletely(loanPaidCompletely);

        return result;
    }
}
