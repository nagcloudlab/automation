package com.upi.dto;

import java.math.BigDecimal;

import com.upi.model.AccountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for account creation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @NotBlank(message = "Account ID is required")
    @Size(min = 4, max = 12, message = "Account ID must be between 4 and 12 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Account ID must contain only uppercase letters and numbers")
    private String accountId;

    @NotBlank(message = "Account holder name is required")
    @Size(min = 2, max = 100, message = "Account holder name must be between 2 and 100 characters")
    private String accountHolderName;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @DecimalMin(value = "0.00", message = "Initial balance cannot be negative")
    @Builder.Default
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Builder.Default
    private AccountType accountType = AccountType.SAVINGS;
}
