package org.salih.banking.converter;

import org.salih.banking.enums.StatusEnum;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.entitiy.Installment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstallmentConverter {
    public List<Installment> generateInstallments(Loan loan) {
        List<Installment> installments = new ArrayList<>();
        LocalDate initialDate = LocalDate.now();

        MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal singleInstallmentAmount = loan.getAmount()
                .divide(BigDecimal.valueOf(loan.getInstallmentCount()), mathContext);

        for (int i = 0; i < loan.getInstallmentCount(); i++) {
            LocalDate dueDate = getNextFirstDayOfMonth(initialDate);
            initialDate = dueDate;
            Installment ins = new Installment();
            ins.setAmount(singleInstallmentAmount);
            ins.setPaidAmount(singleInstallmentAmount);
            ins.setStatus(StatusEnum.CREATED);
            ins.setDueDate(dueDate);
            ins.setLoan(loan);
            installments.add(ins);
        }
        return installments;
    }

    private LocalDate getNextFirstDayOfMonth(LocalDate initialDate) {
        LocalDate nextMonth = initialDate.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }
}
