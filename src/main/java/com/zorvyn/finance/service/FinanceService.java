package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.DashboardSummary;
import com.zorvyn.finance.dto.TransactionDTO;
import com.zorvyn.finance.entity.Transaction;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.repository.TransactionRepository;
import com.zorvyn.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<TransactionDTO> getAllTransactions() {
        User user = getCurrentUser();
        return transactionRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
        return dto;
    }
}
