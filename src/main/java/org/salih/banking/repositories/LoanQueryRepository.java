package org.salih.banking.repositories;

import org.salih.banking.entitiy.Loan;
import org.salih.banking.model.LoanFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanQueryRepository {
    Page<Loan> findLoans(LoanFilter filter, Pageable pageable);
}
