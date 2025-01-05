package org.salih.banking.service.serviceImpl;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import org.salih.banking.converter.InstallmentConverter;
import org.salih.banking.converter.LoanConverter;
import org.salih.banking.entitiy.Installment;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.entitiy.User;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.exception.NoLoanFoundException;
import org.salih.banking.exception.NoUserFoundException;
import org.salih.banking.exception.NotEnoughCreditLimitException;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.model.LoanRequest;
import org.salih.banking.model.PaymentRequest;
import org.salih.banking.model.PaymentResult;
import org.salih.banking.repositories.InstallmentRepository;
import org.salih.banking.repositories.LoanQueryRepository;
import org.salih.banking.repositories.LoanRepository;
import org.salih.banking.repositories.UserRepository;
import org.salih.banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanQueryRepository loanQueryRepository;
    private final UserRepository userRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanConverter loanConverter;
    private final InstallmentConverter installmentConverter;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, LoanQueryRepository loanQueryRepository, UserRepository userRepository, InstallmentRepository installmentRepository, LoanConverter loanConverter, InstallmentConverter installmentConverter) {
        this.loanRepository = loanRepository;
        this.loanQueryRepository = loanQueryRepository;
        this.userRepository = userRepository;
        this.installmentRepository = installmentRepository;
        this.loanConverter = loanConverter;
        this.installmentConverter = installmentConverter;
    }

    public Page<Loan> listLoans(int pageNo, int pageSize, LoanFilter filter) {
        return loanQueryRepository.findLoans(filter, PageRequest.of(pageNo, pageSize));
    }

    @Override
    @Transactional
    public Loan addLoan(LoanRequest loanRequest) throws NoUserFoundException, NotEnoughCreditLimitException {
        User user = getUserById(loanRequest.getUserId());
        loanConverter.validateCreditLimit(user, loanRequest.getAmount());
        Loan loan = createLoan(loanRequest, user);
        updateUserCreditLimit(user, loan.getAmount());
        return loan;
    }

    @Override
    public List<Loan> listLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Override
    public PaymentResult pay(PaymentRequest paymentRequest) throws NoLoanFoundException {
        Loan loan = getLoanById(paymentRequest.getLoanId());
        List<Installment> payableInstallments = loanConverter.getPayableInstallments(loan);

        User user = loan.getUser();
        BigDecimal amountToPay = paymentRequest.getAmount();
        int installmentsPaid = 0;
        BigDecimal totalAmountPaid = BigDecimal.ZERO;

        // Iterate through installments and pay as much as possible
        for (Installment installment : payableInstallments) {
            if (amountToPay.compareTo(installment.getAmount()) >= 0) {
                totalAmountPaid = totalAmountPaid.add(installment.getAmount());
                amountToPay = amountToPay.subtract(installment.getAmount());
                installment.setStatus(StatusEnum.PAID);
                installment.setPaidAmount(installment.getAmount());
                installmentRepository.save(installment);
                installmentsPaid++;
            } else {
                break; // If the current installment cannot be fully paid, stop
            }
        }

        // Update the loan's status if fully paid
        boolean loanPaidCompletely = loan.getInstallments().stream()
                .allMatch(installment -> installment.getStatus() == StatusEnum.PAID);

        if (loanPaidCompletely) {
            loan.setStatus(StatusEnum.PAID);
        }
        loanRepository.save(loan);

        // Update the user's used credit limit
        user.setUsedCreditLimit(user.getUsedCreditLimit().subtract(totalAmountPaid));
        userRepository.save(user);

        PaymentResult result = loanConverter.preparePaymentResult(installmentsPaid, totalAmountPaid, loanPaidCompletely);

        return result;
    }

    private User getUserById(Long userId) throws NoUserFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFoundException("User not found"));
    }

    private Loan getLoanById(Long loanId) throws NoLoanFoundException {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new NoLoanFoundException("User not found"));
    }

    private Loan createLoan(LoanRequest loanRequest, User user) {
        Loan loan = new Loan();
        loan.setStatus(StatusEnum.CREATED);
        loan.setAmount(loanConverter.calculateTotalAmount(loanRequest.getAmount(), loanRequest.getInterestRate()));
        loan.setInstallmentCount(loanRequest.getInstallmentCount());
        loan.setInterestRate(loanRequest.getInterestRate());
        loan.setUser(user);

        List<Installment> installments = installmentConverter.generateInstallments(loan);
        loan.setInstallments(installments);

        return loanRepository.save(loan);
    }

    private void updateUserCreditLimit(User user, BigDecimal loanAmount) {
        user.setUsedCreditLimit(loanConverter.calculateNewUsedCreditLimit(user.getUsedCreditLimit(), loanAmount));
        userRepository.save(user);
    }
}
