package org.salih.banking.builder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.salih.banking.entitiy.Loan;
import org.salih.banking.model.LoanFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanCriteriaBuilder {
    private final EntityManager entityManager;

    @Autowired
    public LoanCriteriaBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public CriteriaQuery<Loan> buildQuery(LoanFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Loan> query = criteriaBuilder.createQuery(Loan.class);
        Root<Loan> root = query.from(Loan.class);

        List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);

        query.where(predicates.toArray(new Predicate[0]));
        return query;
    }

    public List<Predicate> buildPredicates(LoanFilter filter, CriteriaBuilder criteriaBuilder, Root<Loan> root) {
        List<Predicate> predicates = new ArrayList<>();

        filter.getStatus().ifPresent(status -> predicates.add(criteriaBuilder.equal(root.get("status"), status)));
        filter.getCreatedTime().ifPresent(createdTime -> predicates.add(criteriaBuilder.equal(root.get("createdAt"), createdTime)));
        filter.getUserId().ifPresent(userId -> predicates.add(criteriaBuilder.equal(root.get("userId"), userId)));
        filter.getMinAmount().ifPresent(minAmount -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount)));
        filter.getMaxAmount().ifPresent(maxAmount -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount)));
        filter.getInstallmentCount().ifPresent(installmentCount -> predicates.add(criteriaBuilder.equal(root.get("installmentCount"), installmentCount)));

        return predicates;
    }
}
