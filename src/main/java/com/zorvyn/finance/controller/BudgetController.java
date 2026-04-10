package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.ApiResponse;
import com.zorvyn.finance.dto.CategoryBudgetDTO;
import com.zorvyn.finance.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<ApiResponse<CategoryBudgetDTO>> setBudget(@Valid @RequestBody CategoryBudgetDTO dto) {
        CategoryBudgetDTO saved = budgetService.setBudget(dto);
        return ResponseEntity.ok(ApiResponse.success(saved, "Category budget set successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryBudgetDTO>>> getMyBudgets() {
        List<CategoryBudgetDTO> budgets = budgetService.getMyBudgets();
        return ResponseEntity.ok(ApiResponse.success(budgets, "Budgets retrieved successfully"));
    }
}
