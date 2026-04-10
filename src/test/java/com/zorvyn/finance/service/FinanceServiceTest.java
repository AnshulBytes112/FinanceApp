package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.DashboardSummary;
import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.repository.TransactionRepository;
import com.zorvyn.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FinanceService financeService;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetSummary() {
        // Arrange
        String email = "test@example.com";
        User user = User.builder().id(1L).email(email).role(Role.ROLE_ADMIN).build();
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password("pass")
                .authorities("ROLE_ADMIN")
                .build();
        
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(transactionRepository.sumAmountByUserAndType(user, TransactionType.INCOME)).thenReturn(new BigDecimal("1000"));
        when(transactionRepository.sumAmountByUserAndType(user, TransactionType.EXPENSE)).thenReturn(new BigDecimal("400"));

        List<Map<String, Object>> categoryTotals = new ArrayList<>();
        Map<String, Object> foodTotal = new HashMap<>();
        foodTotal.put("category", "Food");
        foodTotal.put("total", new BigDecimal("400"));
        categoryTotals.add(foodTotal);
        when(transactionRepository.sumByCategoryByUser(user)).thenReturn(categoryTotals);

        // Act
        DashboardSummary summary = financeService.getSummary();

        // Assert
        assertEquals(new BigDecimal("1000"), summary.getTotalIncome());
        assertEquals(new BigDecimal("400"), summary.getTotalExpenses());
        assertEquals(new BigDecimal("600"), summary.getNetBalance());
        assertEquals(1, summary.getCategoryWiseTotals().size());
        assertEquals(new BigDecimal("400"), summary.getCategoryWiseTotals().get("Food"));
    }
}
