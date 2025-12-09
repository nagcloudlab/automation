package com.upi.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.upi.dto.ApiResponse;
import com.upi.dto.ApiResponse.ErrorDetails;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for consistent error responses across all controllers.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle AccountNotFoundException
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountNotFound(AccountNotFoundException ex) {
        log.error("Account not found: {}", ex.getAccountId());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("ACCOUNT_NOT_FOUND")
                .field("accountId")
                .rejectedValue(ex.getAccountId())
                .build();
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle InsufficientFundsException
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientFunds(InsufficientFundsException ex) {
        log.error("Insufficient funds: Account={}, Available={}, Requested={}",
                ex.getAccountId(), ex.getAvailableBalance(), ex.getRequestedAmount());
        
        List<String> details = new ArrayList<>();
        details.add("Available balance: " + ex.getAvailableBalance());
        details.add("Requested amount: " + ex.getRequestedAmount());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("INSUFFICIENT_FUNDS")
                .field("amount")
                .rejectedValue(ex.getRequestedAmount())
                .details(details)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle AccountNotActiveException
     */
    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountNotActive(AccountNotActiveException ex) {
        log.error("Account not active: Account={}, Status={}", ex.getAccountId(), ex.getCurrentStatus());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("ACCOUNT_NOT_ACTIVE")
                .field("accountId")
                .rejectedValue(ex.getAccountId())
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle DuplicateAccountException
     */
    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateAccount(DuplicateAccountException ex) {
        log.error("Duplicate account: {}", ex.getAccountId());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("DUPLICATE_ACCOUNT")
                .field("accountId")
                .rejectedValue(ex.getAccountId())
                .build();
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle InvalidTransferException
     */
    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTransfer(InvalidTransferException ex) {
        log.error("Invalid transfer: {}", ex.getMessage());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("INVALID_TRANSFER")
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle TransactionNotFoundException
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTransactionNotFound(TransactionNotFoundException ex) {
        log.error("Transaction not found: {}", ex.getReferenceNumber());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("TRANSACTION_NOT_FOUND")
                .field("referenceNumber")
                .rejectedValue(ex.getReferenceNumber())
                .build();
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .details(details)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetails));
    }

    /**
     * Handle type mismatch errors
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch: {}", ex.getMessage());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("TYPE_MISMATCH")
                .field(ex.getName())
                .rejectedValue(ex.getValue())
                .build();
        
        String message = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, errorDetails));
    }

    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("INTERNAL_ERROR")
                .build();
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred. Please try again later.", errorDetails));
    }
}
