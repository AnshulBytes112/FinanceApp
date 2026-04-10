package com.zorvyn.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request/Response DTO for category-level budgets")
public class CategoryBudgetDTO {
    @Schema(description = "Unique identifier of the budget", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(description = "Name of the category", example = "Groceries")
    private String categoryName;

    @NotNull
    @DecimalMin("0.00")
    @Schema(description = "Maximum spending limit for this category", example = "500.00")
    private BigDecimal budgetAmount;
}
