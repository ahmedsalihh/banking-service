package org.salih.banking.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.enums.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstallmentConverterTest {

    private InstallmentConverter installmentConverter;

    @BeforeEach
    void setUp() {
        installmentConverter = new InstallmentConverter();
    }

    @Test
    void testGenerateInstallments_ValidLoan() {
        // Arrange
        Loan loan = new Loan();
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setInstallmentCount(5);

        // Act
        List<Installment> installments = installmentConverter.generateInstallments(loan);

        // Assert
        assertNotNull(installments);
        assertEquals(5, installments.size());
        installments.forEach(installment -> {
            assertEquals(BigDecimal.valueOf(200), installment.getAmount());
            assertEquals(BigDecimal.valueOf(200), installment.getPaidAmount());
            assertEquals(StatusEnum.CREATED, installment.getStatus());
            assertNotNull(installment.getDueDate());
            assertEquals(loan, installment.getLoan());
        });

        // Verify due dates are the first day of consecutive months
        LocalDate expectedDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);
        for (Installment installment : installments) {
            assertEquals(expectedDate, installment.getDueDate());
            expectedDate = expectedDate.plusMonths(1);
        }
    }
}
