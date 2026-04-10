package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.ApiResponse;
import com.zorvyn.finance.dto.DashboardSummary;
import com.zorvyn.finance.dto.TransactionDTO;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.service.FinanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<ApiResponse<DashboardSummary>> getSummary() {
        DashboardSummary summary = financeService.getSummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Dashboard summary retrieved successfully"));
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<TransactionDTO>>> getFilteredTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "date") Pageable pageable) {
        
        Page<TransactionDTO> transactions = financeService.getFilteredTransactions(
                category, type, minAmount, maxAmount, startDate, endDate, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved successfully"));
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(@Valid @RequestBody TransactionDTO dto) {
        TransactionDTO created = financeService.createTransaction(dto);
        return ResponseEntity.ok(ApiResponse.success(created, "Transaction created successfully"));
    }

    @PutMapping("/transactions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(
            @PathVariable Long id, @Valid @RequestBody TransactionDTO dto) {
        TransactionDTO updated = financeService.updateTransaction(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Transaction updated successfully"));
    }

    @DeleteMapping("/transactions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        financeService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Transaction deleted successfully"));
    }
}
