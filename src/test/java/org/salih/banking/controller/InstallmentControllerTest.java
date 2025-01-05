package org.salih.banking.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstallmentController.class)
class InstallmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstallmentService installmentService;

    @Test
    public void testListInstallmentsByLoanId() throws Exception {
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

        Mockito.when(installmentService.listInstallmentsByLoanId(loanId)).thenReturn(installments);

        mockMvc.perform(get("/installments/list/{loanId}", loanId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(installmentService, times(1)).listInstallmentsByLoanId(loanId);
    }
}