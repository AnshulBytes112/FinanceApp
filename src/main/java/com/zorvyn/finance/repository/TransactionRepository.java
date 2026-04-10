package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.Transaction;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    BigDecimal sumAmountByUserAndType(@Param("user") User user, @Param("type") TransactionType type);

    @Query("SELECT t.category as category, SUM(t.amount) as total FROM Transaction t WHERE t.user = :user GROUP BY t.category")
    List<Map<String, Object>> sumByCategoryByUser(@Param("user") User user);
}
