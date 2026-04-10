package com.zorvyn.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryBudgetDTO {
    private Long id;

    @NotBlank
    private String categoryName;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal budgetAmount;
}
