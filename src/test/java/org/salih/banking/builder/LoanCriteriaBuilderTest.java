package org.salih.banking.builder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.enums.StatusEnum;
import org.salih.banking.model.LoanFilter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class LoanCriteriaBuilderTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Loan> criteriaQuery;

    @Mock
    private Root<Loan> root;

    private LoanCriteriaBuilder loanCriteriaBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanCriteriaBuilder = new LoanCriteriaBuilder(entityManager);
    }

    @Test
    void testBuildQuery() {
        // Arrange
        LoanFilter filter = LoanFilter.builder()
                .status(Optional.of(StatusEnum.CREATED))
                .createdTime(Optional.ofNullable(null))
                .userId(Optional.of(1L))
                .minAmount(Optional.of(BigDecimal.valueOf(1000)))
                .maxAmount(Optional.of(BigDecimal.valueOf(5000)))
                .installmentCount(Optional.of(12))
                .build();

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Loan.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Loan.class)).thenReturn(root);

        // Act
        CriteriaQuery<Loan> query = loanCriteriaBuilder.buildQuery(filter);

        // Assert
        assertNotNull(query);
        verify(criteriaBuilder).createQuery(Loan.class);
        verify(criteriaQuery).from(Loan.class);
    }

    @Test
    void testBuildPredicates() {
        // Arrange
        LoanFilter filter = LoanFilter.builder()
                .status(Optional.of(StatusEnum.CREATED))
                .createdTime(Optional.ofNullable(null))
                .userId(Optional.of(1L))
                .minAmount(Optional.of(BigDecimal.valueOf(1000)))
                .maxAmount(Optional.of(BigDecimal.valueOf(5000)))
                .installmentCount(Optional.of(12))
                .build();

        when(criteriaBuilder.equal(root.get("status"), "APPROVED")).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(root.get("createdAt"), "2025-01-01")).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(root.get("userId"), 1L)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), BigDecimal.valueOf(5000))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(root.get("installmentCount"), 12)).thenReturn(mock(Predicate.class));

        // Act
        List<Predicate> predicates = loanCriteriaBuilder.buildPredicates(filter, criteriaBuilder, root);

        // Assert
        assertNotNull(predicates);
        assertEquals(5, predicates.size());
    }
}
