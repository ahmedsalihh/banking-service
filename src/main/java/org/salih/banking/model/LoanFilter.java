package org.salih.banking.model;

import lombok.Builder;
import lombok.Data;
import org.salih.banking.enums.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
public class LoanFilter {
    private Optional<StatusEnum> status;
    private Optional<LocalDate> createdTime;
    private Optional<Long> userId;
    private Optional<BigDecimal> minAmount;
    private Optional<BigDecimal> maxAmount;
    private Optional<Integer> installmentCount;
}
