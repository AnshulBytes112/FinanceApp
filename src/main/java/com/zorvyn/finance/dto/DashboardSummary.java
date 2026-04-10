package com.zorvyn.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Schema(description = "Overview of the user's financial status")
public class DashboardSummary {
    @Schema(description = "Total income accumulated", example = "5000.00")
    private BigDecimal totalIncome;

    @Schema(description = "Total expenses accumulated", example = "3200.00")
    private BigDecimal totalExpenses;

    @Schema(description = "Current net balance (Income - Expenses)", example = "1800.00")
    private BigDecimal netBalance;

    @Schema(description = "Breakdown of expenses by category")
    private Map<String, BigDecimal> categoryWiseTotals;

    @Schema(description = "List of categories with budget alert statuses")
    private List<BudgetStatusDTO> budgetStatus;
}
