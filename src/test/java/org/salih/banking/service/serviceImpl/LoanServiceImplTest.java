package org.salih.banking.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.salih.banking.converter.InstallmentConverter;
import org.salih.banking.converter.LoanConverter;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.entitiy.User;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.model.LoanRequest;
import org.salih.banking.repositories.LoanQueryRepository;
import org.salih.banking.repositories.LoanRepository;
import org.salih.banking.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private LoanQueryRepository loanQueryRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoanConverter loanConverter;
    @Mock
    private InstallmentConverter installmentConverter;

    @InjectMocks
    private LoanServiceImpl loanService;

    public LoanServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listLoans() {
        int pageNo = 0;
        int pageSize = 10;
        LoanFilter filter = LoanFilter.builder()
                .status(Optional.of(StatusEnum.CREATED))
                .minAmount(Optional.of(BigDecimal.valueOf(1000)))
                .maxAmount(Optional.of(BigDecimal.valueOf(5000)))
                .createdTime(Optional.of(LocalDate.of(2023, 1, 1)))
                .installmentCount(Optional.of(12))
                .build();

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Loan loan = new Loan(1L, StatusEnum.CREATED, 0.2f, BigDecimal.valueOf(2000),
                12, null, Collections.emptyList(), LocalDate.of(2023, 1, 5), null);

        List<Loan> loanList = List.of(loan);
        Page<Loan> pagedLoans = new PageImpl<>(loanList, pageable, loanList.size());

        when(loanQueryRepository.findLoans(filter, pageable)).thenReturn(pagedLoans);

        Page<Loan> result = loanService.listLoans(pageNo, pageSize, filter);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(StatusEnum.CREATED);
        assertThat(result.getContent().get(0).getAmount()).isEqualTo(BigDecimal.valueOf(2000));

        verify(loanQueryRepository, times(1)).findLoans(filter, pageable);
    }

    @Test
    void addLoan() throws Exception {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId(1L);
        loanRequest.setAmount(BigDecimal.valueOf(2000));
        loanRequest.setInterestRate(0.2f);
        loanRequest.setInstallmentCount(12);

        User user = new User();
        user.setId(1L);
        user.setCreditLimit(BigDecimal.valueOf(5000));

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setAmount(loanRequest.getAmount());
        loan.setInstallmentCount(loanRequest.getInstallmentCount());

        Installment installment = new Installment();
        installment.setLoan(loan);
        installment.setId(1L);

        List<Installment> installments = new ArrayList<>();
        installments.add(installment);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(loanConverter).validateCreditLimit(user, loanRequest.getAmount());
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(installmentConverter.generateInstallments(any(Loan.class))).thenReturn(installments);

        Loan result = loanService.addLoan(loanRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(2000));
        verify(loanConverter, times(1)).validateCreditLimit(user, loanRequest.getAmount());
        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    void listCreditsByUserId() {
        List<Loan> loanList = new ArrayList<>();
        Loan c = new Loan();
        c.setInstallmentCount(3);
        c.setId(1);
        c.setAmount(BigDecimal.valueOf(10));
        loanList.add(c);

        loanService.listLoansByUserId(1L);

        when(loanRepository.findByUserId(Mockito.anyLong())).thenReturn(loanList);

        assertNotEquals(loanList.size(), 0);
    }
}