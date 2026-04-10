package com.zorvyn.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Comparison of budget vs actual spending for a category")
public class BudgetStatusDTO {
    @Schema(description = "Category name", example = "Food")
    private String category;

    @Schema(description = "Planned budget amount", example = "500.00")
    private BigDecimal budgetAmount;

    @Schema(description = "Actual spending amount", example = "550.00")
    private BigDecimal actualAmount;

    @Schema(description = "Flag indicating if spending has exceeded the budget")
    private boolean overBudget;
}
