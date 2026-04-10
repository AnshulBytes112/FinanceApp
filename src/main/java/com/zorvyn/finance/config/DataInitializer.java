package com.zorvyn.finance.config;

import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.Transaction;
import com.zorvyn.finance.entity.TransactionType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.repository.TransactionRepository;
import com.zorvyn.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create Admin
            User admin = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);

            // Create Analyst
            User analyst = User.builder()
                    .email("analyst@example.com")
                    .password(passwordEncoder.encode("analyst123"))
                    .role(Role.ROLE_ANALYST)
                    .active(true)
                    .build();
            userRepository.save(analyst);

            // Create Viewer
            User viewer = User.builder()
                    .email("viewer@example.com")
                    .password(passwordEncoder.encode("viewer123"))
                    .role(Role.ROLE_VIEWER)
                    .active(true)
                    .build();
            userRepository.save(viewer);

            // Seed some data for Admin
            transactionRepository.save(Transaction.builder()
                    .amount(new BigDecimal("5000"))
                    .type(TransactionType.INCOME)
                    .category("Salary")
                    .date(LocalDate.now().minusDays(5))
                    .notes("Monthly salary")
                    .user(admin)
                    .build());

            transactionRepository.save(Transaction.builder()
                    .amount(new BigDecimal("150"))
                    .type(TransactionType.EXPENSE)
                    .category("Food")
                    .date(LocalDate.now().minusDays(2))
                    .notes("Dinner with friends")
                    .user(admin)
                    .build());

            transactionRepository.save(Transaction.builder()
                    .amount(new BigDecimal("1200"))
                    .type(TransactionType.EXPENSE)
                    .category("Rent")
                    .date(LocalDate.now().minusDays(10))
                    .notes("Monthly rent")
                    .user(admin)
                    .build());
        }
    }
}
