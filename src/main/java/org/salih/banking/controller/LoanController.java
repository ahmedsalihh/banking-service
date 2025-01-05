package org.salih.banking.controller;

import jakarta.validation.Valid;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.exception.NoLoanFoundException;
import org.salih.banking.exception.NoUserFoundException;
import org.salih.banking.exception.NotEnoughCreditLimitException;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.model.LoanRequest;
import org.salih.banking.model.PaymentRequest;
import org.salih.banking.model.PaymentResult;
import org.salih.banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Loan>> listLoans(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Optional<StatusEnum> status,
            @RequestParam(required = false) Optional<LocalDate> createdTime,
            @RequestParam(required = false) Optional<Long> userId,
            @RequestParam(required = false) Optional<BigDecimal> minAmount,
            @RequestParam(required = false) Optional<BigDecimal> maxAmount,
            @RequestParam(required = false) Optional<Integer> installmentCount) {

        LoanFilter filter = LoanFilter.builder()
                .status(status)
                .createdTime(createdTime)
                .userId(userId)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .installmentCount(installmentCount)
                .build();

        Page<Loan> loans = loanService.listLoans(pageNo, pageSize, filter);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/add")
    public ResponseEntity<Loan> addLoan(@Valid @RequestBody LoanRequest loanRequest) throws NoUserFoundException, NotEnoughCreditLimitException {
        return ResponseEntity.ok(loanService.addLoan(loanRequest));
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResult> pay(@Valid @RequestBody PaymentRequest paymentRequest) throws NoLoanFoundException {
        return ResponseEntity.ok(loanService.pay(paymentRequest));
    }
}
