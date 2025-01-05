package org.salih.banking.service.serviceImpl;

import org.salih.banking.entitiy.Installment;
import org.salih.banking.repositories.InstallmentRepository;
import org.salih.banking.service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepository installmentRepository;

    @Autowired
    public InstallmentServiceImpl(InstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
    }

    @Override
    public List<Installment> listInstallmentsByLoanId(Long loanId) {
        return installmentRepository.findByLoanId(loanId);
    }
}
