package com.zorvyn.finance.dto;

import com.zorvyn.finance.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Data Transfer Object for financial transactions")
public class TransactionDTO {
    @Schema(description = "Unique identifier of the transaction", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @DecimalMin("0.01")
    @Schema(description = "Amount of the transaction", example = "49.99")
    private BigDecimal amount;

    @NotNull
    @Schema(description = "Type of transaction (INCOME or EXPENSE)")
    private TransactionType type;

    @NotBlank
    @Schema(description = "Category of the transaction", example = "Food")
    private String category;

    @NotNull
    @Schema(description = "Date of the transaction")
    private LocalDate date;

    @Schema(description = "Optional notes or description", example = "Grocery shopping at Walmart")
    private String notes;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Version for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;
}
