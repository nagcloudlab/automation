package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * POJO representing the Account creation request.
 * Maps to: POST /api/v1/accounts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRequest {
    
    private String accountId;
    private String accountHolderName;
    private String email;
    private String phone;
    private BigDecimal initialBalance;
    private String accountType;

    /**
     * Factory method for creating a valid account request
     */
    public static AccountRequest createValid(String accountId, String email, String phone) {
        return AccountRequest.builder()
                .accountId(accountId)
                .accountHolderName("Test User")
                .email(email)
                .phone(phone)
                .initialBalance(new BigDecimal("1000.00"))
                .accountType("SAVINGS")
                .build();
    }

    /**
     * Factory method for creating a minimal account request
     */
    public static AccountRequest createMinimal(String accountId) {
        return AccountRequest.builder()
                .accountId(accountId)
                .accountHolderName("Minimal User")
                .initialBalance(BigDecimal.ZERO)
                .build();
    }
}
