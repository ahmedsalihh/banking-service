package org.salih.banking.service;

import org.salih.banking.entitiy.Installment;

import java.util.List;

public interface InstallmentService {

    List<Installment> listInstallmentsByLoanId(Long creditId);
}
