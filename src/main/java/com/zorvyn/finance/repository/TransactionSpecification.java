package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.Transaction;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionSpecification {

    public static Specification<Transaction> hasUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Transaction> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null : cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> amountGreaterThan(BigDecimal amount) {
        return (root, query, cb) -> amount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), amount);
    }

    public static Specification<Transaction> amountLessThan(BigDecimal amount) {
        return (root, query, cb) -> amount == null ? null : cb.lessThanOrEqualTo(root.get("amount"), amount);
    }

    public static Specification<Transaction> dateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null) return cb.between(root.get("date"), start, end);
            if (start != null) return cb.greaterThanOrEqualTo(root.get("date"), start);
            return cb.lessThanOrEqualTo(root.get("date"), end);
        };
    }
}
