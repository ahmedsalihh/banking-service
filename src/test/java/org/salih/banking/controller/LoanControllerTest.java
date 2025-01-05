package org.salih.banking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.model.LoanRequest;
import org.salih.banking.model.PaymentRequest;
import org.salih.banking.model.PaymentResult;
import org.salih.banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Test
    public void testListLoans() throws Exception {
        int pageNo = 0;
        int pageSize = 10;
        StatusEnum status = StatusEnum.CREATED;
        LocalDate createdTime = LocalDate.of(2025, 1, 1);
        Long userId = 1L;
        BigDecimal minAmount = new BigDecimal("1000");
        BigDecimal maxAmount = new BigDecimal("5000");
        int installmentCount = 12;

        Loan loan1 = new Loan();
        loan1.setId(1l);
        loan1.setStatus(StatusEnum.CREATED);
        loan1.setInterestRate(0.5f);
        loan1.setAmount(BigDecimal.valueOf(3000));
        loan1.setInstallmentCount(6);
        Loan loan2 = new Loan();
        loan2.setId(2l);
        loan2.setStatus(StatusEnum.CREATED);
        loan2.setInterestRate(0.2f);
        loan2.setAmount(BigDecimal.valueOf(5000));
        loan2.setInstallmentCount(12);

        LoanFilter filter = LoanFilter.builder()
                .status(Optional.of(status))
                .createdTime(Optional.of(createdTime))
                .userId(Optional.of(userId))
                .minAmount(Optional.of(minAmount))
                .maxAmount(Optional.of(maxAmount))
                .installmentCount(Optional.of(installmentCount))
                .build();

        Page<Loan> loans = new PageImpl<>(Arrays.asList(loan1, loan2));

        when(loanService.listLoans(pageNo, pageSize, filter)).thenReturn(loans);

        mockMvc.perform(get("/loans/list")
                        .param("pageNo", String.valueOf(pageNo))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("status", status.name())
                        .param("createdTime", createdTime.toString())
                        .param("userId", String.valueOf(userId))
                        .param("minAmount", minAmount.toString())
                        .param("maxAmount", maxAmount.toString())
                        .param("installmentCount", String.valueOf(installmentCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].amount", is(3000)))
                .andExpect(jsonPath("$.content[0].status", is(status.name())))
                .andExpect(jsonPath("$.content[0].installmentCount", is(6)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].amount", is(5000)));

        verify(loanService, times(1)).listLoans(pageNo, pageSize, filter);
    }

    @Test
    public void testAddLoan() throws Exception {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId(1L);
        loanRequest.setAmount(new BigDecimal("5000"));
        loanRequest.setInterestRate(5.5f);
        loanRequest.setInstallmentCount(12);

        Loan loan1 = new Loan();
        loan1.setId(1l);
        loan1.setStatus(StatusEnum.CREATED);
        loan1.setInterestRate(0.5f);
        loan1.setAmount(BigDecimal.valueOf(3000));
        loan1.setInstallmentCount(6);

        when(loanService.addLoan(any(LoanRequest.class))).thenReturn(loan1);

        mockMvc.perform(post("/loans/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userId\": 1," +
                                "\"amount\": 3000," +
                                "\"interestRate\": 0.5," +
                                "\"installmentCount\": 6" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(3000)))
                .andExpect(jsonPath("$.installmentCount", is(6)))
                .andExpect(jsonPath("$.status", is("CREATED")));

        verify(loanService, times(1)).addLoan(any(LoanRequest.class));
    }

    @Test
    public void testPayLoan() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLoanId(1L);
        paymentRequest.setAmount(new BigDecimal("1000"));

        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setInstallmentsPaid(1);
        paymentResult.setTotalAmountPaid(new BigDecimal("1000"));
        paymentResult.setLoanPaidCompletely(false);

        when(loanService.pay(any(PaymentRequest.class))).thenReturn(paymentResult);

        mockMvc.perform(post("/loans/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"loanId\": 1," +
                                "\"amount\": 1000" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.installmentsPaid", is(1)))
                .andExpect(jsonPath("$.totalAmountPaid", is(1000)))
                .andExpect(jsonPath("$.loanPaidCompletely", is(false)));

        verify(loanService, times(1)).pay(any(PaymentRequest.class));
    }
}