package com.upi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.upi.model.Account;
import com.upi.model.AccountStatus;
import com.upi.model.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for account information.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private String accountId;
    private String accountHolderName;
    private String email;
    private String phone;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Convert Account entity to AccountResponse DTO
     */
    public static AccountResponse fromEntity(Account account) {
        if (account == null) {
            return null;
        }
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountHolderName(account.getAccountHolderName())
                .email(account.getEmail())
                .phone(account.getPhone())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
