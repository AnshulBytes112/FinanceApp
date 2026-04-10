package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.CategoryBudget;
import com.zorvyn.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
    List<CategoryBudget> findByUser(User user);
    Optional<CategoryBudget> findByUserAndCategoryNameIgnoreCase(User user, String categoryName);
}
