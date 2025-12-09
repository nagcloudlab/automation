package com.upi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.upi.model.Transaction;
import com.upi.model.TransactionStatus;
import com.upi.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for transaction details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private Long transactionId;
    private String referenceNumber;
    private String fromAccountId;
    private String fromAccountName;
    private String toAccountId;
    private String toAccountName;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private String failureReason;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    /**
     * Convert Transaction entity to TransactionResponse DTO
     */
    public static TransactionResponse fromEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .referenceNumber(transaction.getReferenceNumber())
                .fromAccountId(transaction.getFromAccount().getAccountId())
                .fromAccountName(transaction.getFromAccount().getAccountHolderName())
                .toAccountId(transaction.getToAccount().getAccountId())
                .toAccountName(transaction.getToAccount().getAccountHolderName())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .failureReason(transaction.getFailureReason())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
