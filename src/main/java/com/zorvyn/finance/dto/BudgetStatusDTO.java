package com.zorvyn.finance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetStatusDTO {
    private String category;
    private BigDecimal budgetAmount;
    private BigDecimal actualAmount;
    private boolean overBudget;
}
