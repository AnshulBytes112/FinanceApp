package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.CategoryBudgetDTO;
import com.zorvyn.finance.entity.CategoryBudget;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.repository.CategoryBudgetRepository;
import com.zorvyn.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private CategoryBudgetRepository categoryBudgetRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public CategoryBudgetDTO setBudget(CategoryBudgetDTO dto) {
        User user = getCurrentUser();
        Optional<CategoryBudget> existing = categoryBudgetRepository.findByUserAndCategoryNameIgnoreCase(user, dto.getCategoryName());
        
        CategoryBudget budget;
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setBudgetAmount(dto.getBudgetAmount());
        } else {
            budget = CategoryBudget.builder()
                    .categoryName(dto.getCategoryName())
                    .budgetAmount(dto.getBudgetAmount())
                    .user(user)
                    .build();
        }
        
        CategoryBudget saved = categoryBudgetRepository.save(budget);
        return mapToDTO(saved);
    }

    public List<CategoryBudgetDTO> getMyBudgets() {
        User user = getCurrentUser();
        return categoryBudgetRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CategoryBudgetDTO mapToDTO(CategoryBudget b) {
        CategoryBudgetDTO dto = new CategoryBudgetDTO();
        dto.setId(b.getId());
        dto.setCategoryName(b.getCategoryName());
        dto.setBudgetAmount(b.getBudgetAmount());
        return dto;
    }
}
