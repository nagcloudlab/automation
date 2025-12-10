package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing the Account update request.
 * Maps to: PUT /api/v1/accounts/{accountId}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountUpdateRequest {
    
    private String accountHolderName;
    private String email;
    private String phone;
    private String status;

    /**
     * Factory method for creating name update request
     */
    public static AccountUpdateRequest updateName(String newName) {
        return AccountUpdateRequest.builder()
                .accountHolderName(newName)
                .build();
    }

    /**
     * Factory method for creating status update request
     */
    public static AccountUpdateRequest updateStatus(String status) {
        return AccountUpdateRequest.builder()
                .status(status)
                .build();
    }

    /**
     * Factory method for creating full update request
     */
    public static AccountUpdateRequest fullUpdate(String name, String email, String phone, String status) {
        return AccountUpdateRequest.builder()
                .accountHolderName(name)
                .email(email)
                .phone(phone)
                .status(status)
                .build();
    }
}
