package org.salih.banking.repositories;

import org.salih.banking.entitiy.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(long id);
}
