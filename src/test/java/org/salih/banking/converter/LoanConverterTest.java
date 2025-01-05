package org.salih.banking.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.entitiy.User;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.exception.NotEnoughCreditLimitException;
import org.salih.banking.model.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanConverterTest {

    private LoanConverter loanConverter;

    @BeforeEach
    void setUp() {
        loanConverter = new LoanConverter();
    }

    @Test
    void testCalculateTotalAmount_ValidInputs() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        float interestRate = 0.2f;

        BigDecimal result = loanConverter.calculateTotalAmount(amount, interestRate);

        assertEquals(BigDecimal.valueOf(1200.00).setScale(2), result);
    }

    @Test
    void testCalculateTotalAmount_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateTotalAmount(BigDecimal.ZERO, 0.2f));
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateTotalAmount(null, 0.2f));
    }

    @Test
    void testCalculateTotalAmount_InvalidInterestRate() {
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateTotalAmount(BigDecimal.valueOf(1000), 0.05f));
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateTotalAmount(BigDecimal.valueOf(1000), 0.6f));
    }

    @Test
    void testCheckUserCredibility_ValidInputs() {
        BigDecimal creditLimit = BigDecimal.valueOf(5000);
        BigDecimal usedCredit = BigDecimal.valueOf(1000);
        BigDecimal loanAmount = BigDecimal.valueOf(2000);

        boolean result = loanConverter.checkUserCredibility(creditLimit, usedCredit, loanAmount);

        assertTrue(result);
    }

    @Test
    void testCheckUserCredibility_InvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> loanConverter.checkUserCredibility(null, BigDecimal.valueOf(1000), BigDecimal.valueOf(2000)));
        assertThrows(IllegalArgumentException.class, () -> loanConverter.checkUserCredibility(BigDecimal.valueOf(5000), null, BigDecimal.valueOf(2000)));
        assertThrows(IllegalArgumentException.class, () -> loanConverter.checkUserCredibility(BigDecimal.valueOf(5000), BigDecimal.valueOf(1000), null));
    }

    @Test
    void testCalculateNewUsedCreditLimit() {
        BigDecimal currentUsedCreditLimit = BigDecimal.valueOf(1000);
        BigDecimal loanAmount = BigDecimal.valueOf(2000);

        BigDecimal result = loanConverter.calculateNewUsedCreditLimit(currentUsedCreditLimit, loanAmount);

        assertEquals(BigDecimal.valueOf(3000), result);
    }

    @Test
    void testCalculateNewUsedCreditLimit_InvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateNewUsedCreditLimit(null, BigDecimal.valueOf(2000)));
        assertThrows(IllegalArgumentException.class, () -> loanConverter.calculateNewUsedCreditLimit(BigDecimal.valueOf(1000), null));
    }

    @Test
    void testValidateCreditLimit_SufficientCredit() {
        User user = new User();
        user.setCreditLimit(BigDecimal.valueOf(5000));
        user.setUsedCreditLimit(BigDecimal.valueOf(1000));
        BigDecimal loanAmount = BigDecimal.valueOf(2000);

        assertDoesNotThrow(() -> loanConverter.validateCreditLimit(user, loanAmount));
    }

    @Test
    void testValidateCreditLimit_InsufficientCredit() {
        User user = new User();
        user.setCreditLimit(BigDecimal.valueOf(3000));
        user.setUsedCreditLimit(BigDecimal.valueOf(2000));
        BigDecimal loanAmount = BigDecimal.valueOf(2000);

        assertThrows(NotEnoughCreditLimitException.class, () -> loanConverter.validateCreditLimit(user, loanAmount));
    }

    @Test
    void testGetPayableInstallments() {
        Loan loan = new Loan();
        Installment installment1 = new Installment(StatusEnum.CREATED, LocalDate.now().plusMonths(1));
        Installment installment2 = new Installment(StatusEnum.CREATED, LocalDate.now().plusMonths(2));
        Installment installment3 = new Installment(StatusEnum.CREATED, LocalDate.now().plusMonths(4));
        Installment installment4 = new Installment(StatusEnum.PAID, LocalDate.now().plusMonths(3));

        loan.setInstallments(Arrays.asList(installment1, installment2, installment3, installment4));

        List<Installment> result = loanConverter.getPayableInstallments(loan);

        assertEquals(2, result.size());
        assertTrue(result.contains(installment1));
        assertTrue(result.contains(installment2));
    }

    @Test
    void testPreparePaymentResult() {
        int installmentsPaid = 3;
        BigDecimal totalAmountPaid = BigDecimal.valueOf(1500);
        boolean loanPaidCompletely = true;

        PaymentResult result = loanConverter.preparePaymentResult(installmentsPaid, totalAmountPaid, loanPaidCompletely);

        assertEquals(installmentsPaid, result.getInstallmentsPaid());
        assertEquals(totalAmountPaid, result.getTotalAmountPaid());
        assertTrue(result.isLoanPaidCompletely());
    }
}
