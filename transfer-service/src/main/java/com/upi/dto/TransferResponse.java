package com.upi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for fund transfer operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponse {

    private String transactionId;
    private String referenceNumber;
    private String status;
    private String message;
    
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    
    private BigDecimal fromAccountBalance;
    private BigDecimal toAccountBalance;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    /**
     * Create a success response
     */
    public static TransferResponse success(String referenceNumber, String message) {
        return TransferResponse.builder()
                .referenceNumber(referenceNumber)
                .status("SUCCESS")
                .message(message)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    /**
     * Create a failure response
     */
    public static TransferResponse failure(String message) {
        return TransferResponse.builder()
                .status("FAILED")
                .message(message)
                .transactionDate(LocalDateTime.now())
                .build();
    }
}

