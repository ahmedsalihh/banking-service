package org.salih.banking.service;

import org.salih.banking.entitiy.Loan;
import org.salih.banking.exception.NoLoanFoundException;
import org.salih.banking.exception.NoUserFoundException;
import org.salih.banking.exception.NotEnoughCreditLimitException;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.model.LoanRequest;
import org.salih.banking.model.PaymentRequest;
import org.salih.banking.model.PaymentResult;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoanService {
    Page<Loan> listLoans(int pageNo, int pageSize, LoanFilter filter);

    Loan addLoan(LoanRequest loanRequest) throws NoUserFoundException, NotEnoughCreditLimitException;

    List<Loan> listLoansByUserId(Long userId);

    PaymentResult pay(PaymentRequest paymentRequest) throws NoLoanFoundException;
}
