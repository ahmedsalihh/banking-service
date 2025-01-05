package org.salih.banking.repositories;

import org.salih.banking.entitiy.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByLoanId(Long loanId);
}
