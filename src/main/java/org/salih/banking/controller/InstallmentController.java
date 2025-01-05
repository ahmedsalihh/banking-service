package org.salih.banking.controller;

import org.salih.banking.entitiy.Installment;
import org.salih.banking.service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/installments")
public class InstallmentController {

    private final InstallmentService installmentService;

    @Autowired
    public InstallmentController(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @GetMapping("/list/{loanId}")
    public ResponseEntity<List<Installment>> listInstallmentsByLoanId(@PathVariable("loanId") Long loanId) {
        return ResponseEntity.ok(installmentService.listInstallmentsByLoanId(loanId));
    }
}
