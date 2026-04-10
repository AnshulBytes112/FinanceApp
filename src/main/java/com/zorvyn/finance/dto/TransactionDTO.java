package com.zorvyn.finance.dto;

import com.zorvyn.finance.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
