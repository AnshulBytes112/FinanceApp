package com.zorvyn.finance.entity;

import com.zorvyn.finance.config.BeanUtil;
import com.zorvyn.finance.repository.AuditLogRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class TransactionAuditListener {

    @PostPersist
    public void onPostPersist(Transaction transaction) {
        logAudit(transaction, "CREATE");
    }

    @PostUpdate
    public void onPostUpdate(Transaction transaction) {
        logAudit(transaction, "UPDATE");
    }

    @PostRemove
    public void onPostRemove(Transaction transaction) {
        logAudit(transaction, "DELETE");
    }

    private void logAudit(Transaction transaction, String action) {
        AuditLogRepository repository = BeanUtil.getBean(AuditLogRepository.class);
        
        String user = "SYSTEM";
        Object principal = SecurityContextHolder.getContext().getAuthentication() != null ? 
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
        
        if (principal instanceof UserDetails) {
            user = ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            user = principal.toString();
        }

        AuditLog log = AuditLog.builder()
                .action(action)
                .entityName("Transaction")
                .entityId(transaction.getId())
                .performedBy(user)
                .timestamp(LocalDateTime.now())
                .details(String.format("Amount: %s, Type: %s, Category: %s", 
                        transaction.getAmount(), transaction.getType(), transaction.getCategory()))
                .build();

        repository.save(log);
    }
}
