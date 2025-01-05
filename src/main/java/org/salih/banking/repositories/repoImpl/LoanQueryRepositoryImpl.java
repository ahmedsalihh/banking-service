package org.salih.banking.repositories.repoImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.salih.banking.builder.LoanCriteriaBuilder;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.model.LoanFilter;
import org.salih.banking.repositories.LoanQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoanQueryRepositoryImpl implements LoanQueryRepository {

    private final EntityManager entityManager;
    private final LoanCriteriaBuilder loanCriteriaBuilder;

    @Autowired
    public LoanQueryRepositoryImpl(EntityManager entityManager, LoanCriteriaBuilder loanCriteriaBuilder) {
        this.entityManager = entityManager;
        this.loanCriteriaBuilder = loanCriteriaBuilder;
    }

    @Override
    public Page<Loan> findLoans(LoanFilter filter, Pageable pageable) {
        CriteriaQuery<Loan> query = loanCriteriaBuilder.buildQuery(filter);
        TypedQuery<Loan> typedQuery = entityManager.createQuery(query);

        // Pagination
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Fetch results and count
        List<Loan> loans = typedQuery.getResultList();
        long totalRecords = getTotalRecords(filter);

        return new PageImpl<>(loans, pageable, totalRecords);
    }

    private long getTotalRecords(LoanFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Loan> root = countQuery.from(Loan.class);

        List<Predicate> predicates = loanCriteriaBuilder.buildPredicates(filter, criteriaBuilder, root);
        countQuery.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
