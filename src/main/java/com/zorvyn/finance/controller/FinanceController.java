package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.DashboardSummary;
import com.zorvyn.finance.dto.TransactionDTO;
import com.zorvyn.finance.service.FinanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(financeService.getSummary());
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(financeService.getAllTransactions());
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(financeService.createTransaction(dto));
    }

    @PutMapping("/transactions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(financeService.updateTransaction(id, dto));
    }

    @DeleteMapping("/transactions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        financeService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
