package org.salih.banking.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.repositories.InstallmentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InstallmentServiceImplTest {

    @Mock
    private InstallmentRepository installmentRepository;

    @InjectMocks
    private InstallmentServiceImpl installmentService;

    public InstallmentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListInstallmentsByLoanId() {
        Long loanId = 1L;
        Installment i1 = new Installment();
        i1.setId(1l);
        i1.setAmount(new BigDecimal("500.0"));
        i1.setDueDate(LocalDate.parse("2025-01-01"));

        Installment i2 = new Installment();
        i2.setId(2l);
        i2.setAmount(new BigDecimal("500.0"));
        i2.setDueDate(LocalDate.parse("2025-01-01"));

        List<Installment> installments = new ArrayList<>();
        installments.add(i1);
        installments.add(i2);

        when(installmentRepository.findByLoanId(loanId)).thenReturn(installments);

        List<Installment> result = installmentService.listInstallmentsByLoanId(loanId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getDueDate()).isEqualTo("2025-01-01");
        verify(installmentRepository, times(1)).findByLoanId(loanId);
    }

}