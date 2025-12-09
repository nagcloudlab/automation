package com.upi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction Entity - Represents a fund transfer transaction between accounts.
 * Maintains complete audit trail of all transfer operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_from_account", columnList = "from_account_id"),
    @Index(name = "idx_transaction_to_account", columnList = "to_account_id"),
    @Index(name = "idx_transaction_reference", columnList = "reference_number"),
    @Index(name = "idx_transaction_date", columnList = "transaction_date")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "reference_number", nullable = false, unique = true, length = 20)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    @Builder.Default
    private TransactionType transactionType = TransactionType.FUND_TRANSFER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @Column(name = "from_balance_before", precision = 15, scale = 2)
    private BigDecimal fromBalanceBefore;

    @Column(name = "from_balance_after", precision = 15, scale = 2)
    private BigDecimal fromBalanceAfter;

    @Column(name = "to_balance_before", precision = 15, scale = 2)
    private BigDecimal toBalanceBefore;

    @Column(name = "to_balance_after", precision = 15, scale = 2)
    private BigDecimal toBalanceAfter;

    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
        if (referenceNumber == null) {
            referenceNumber = generateReferenceNumber();
        }
    }

    /**
     * Generate a unique reference number for the transaction.
     * Format: TXN + timestamp + random suffix
     */
    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    /**
     * Mark transaction as completed successfully
     */
    public void markCompleted() {
        this.status = TransactionStatus.COMPLETED;
    }

    /**
     * Mark transaction as failed with reason
     */
    public void markFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
    }
}
