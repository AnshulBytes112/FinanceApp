package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.DashboardSummary;
import com.zorvyn.finance.dto.TransactionDTO;
import com.zorvyn.finance.entity.Transaction;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.repository.TransactionRepository;
import com.zorvyn.finance.repository.TransactionSpecification;
import com.zorvyn.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        User user = getCurrentUser();
        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .type(dto.getType())
                .category(dto.getCategory())
                .date(dto.getDate())
                .notes(dto.getNotes())
                .user(user)
                .build();
        
        Transaction saved = transactionRepository.save(transaction);
        return mapToDTO(saved);
    }

    public Page<TransactionDTO> getFilteredTransactions(
            String category, TransactionType type, BigDecimal minAmount, BigDecimal maxAmount,
            LocalDate startDate, LocalDate endDate, Pageable pageable) {
        
        User user = getCurrentUser();
        Specification<Transaction> spec = Specification.where(TransactionSpecification.hasUser(user))
                .and(TransactionSpecification.hasCategory(category))
                .and(TransactionSpecification.hasType(type))
                .and(TransactionSpecification.amountGreaterThan(minAmount))
                .and(TransactionSpecification.amountLessThan(maxAmount))
                .and(TransactionSpecification.dateBetween(startDate, endDate));

        return transactionRepository.findAll(spec, pageable).map(this::mapToDTO);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO dto) {
        User user = getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this transaction");
        }

        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate());
        transaction.setNotes(dto.getNotes());
        transaction.setVersion(dto.getVersion());

        return mapToDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User user = getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this transaction");
        }

        // Hibernate @SQLDelete will handle the soft delete
        transactionRepository.delete(transaction);
    }

    public DashboardSummary getSummary() {
        User user = getCurrentUser();
        BigDecimal income = transactionRepository.sumAmountByUserAndType(user, TransactionType.INCOME);
        BigDecimal expenses = transactionRepository.sumAmountByUserAndType(user, TransactionType.EXPENSE);
        
        income = income != null ? income : BigDecimal.ZERO;
        expenses = expenses != null ? expenses : BigDecimal.ZERO;

        List<Map<String, Object>> categoryTotalsRaw = transactionRepository.sumByCategoryByUser(user);
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Map<String, Object> map : categoryTotalsRaw) {
            categoryTotals.put((String) map.get("category"), (BigDecimal) map.get("total"));
        }

        return DashboardSummary.builder()
                .totalIncome(income)
                .totalExpenses(expenses)
                .netBalance(income.subtract(expenses))
                .categoryWiseTotals(categoryTotals)
                .build();
    }

    private TransactionDTO mapToDTO(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setType(t.getType());
        dto.setCategory(t.getCategory());
        dto.setDate(t.getDate());
        dto.setNotes(t.getNotes());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        dto.setVersion(t.getVersion());
        return dto;
    }
}
