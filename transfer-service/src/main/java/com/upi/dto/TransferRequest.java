package com.upi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for fund transfer operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @NotBlank(message = "Source account ID is required")
    @Size(min = 4, max = 12, message = "Account ID must be between 4 and 12 characters")
    private String fromAccId;

    @NotBlank(message = "Destination account ID is required")
    @Size(min = 4, max = 12, message = "Account ID must be between 4 and 12 characters")
    private String toAccId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}

