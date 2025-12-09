package com.upi.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upi.dto.ApiResponse;
import com.upi.dto.TransactionResponse;
import com.upi.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for transaction history operations.
 * Provides endpoints for querying transaction history and audit trails.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction History", description = "APIs for viewing transaction history and audit trails")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
        summary = "Get transaction by reference number",
        description = "Retrieves transaction details for the specified reference number"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Transaction found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Transaction not found")
    })
    @GetMapping("/{referenceNumber}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(
            @Parameter(description = "Transaction reference number", example = "TXN1001")
            @PathVariable String referenceNumber) {
        
        log.debug("Fetching transaction: {}", referenceNumber);
        
        TransactionResponse response = transactionService.getTransaction(referenceNumber);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
        summary = "Get all transactions for an account",
        description = "Retrieves all transactions (sent and received) for the specified account"
    )
    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByAccount(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.debug("Fetching transactions for account: {}", accountId);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccount(accountId);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @Operation(
        summary = "Get paginated transactions for an account",
        description = "Retrieves transactions with pagination support"
    )
    @GetMapping("/account/{accountId}/paginated")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactionsByAccountPaginated(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("Fetching paginated transactions for account: {}, page: {}, size: {}", accountId, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        Page<TransactionResponse> transactions = transactionService.getTransactionsByAccountPaginated(accountId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @Operation(
        summary = "Get sent (debit) transactions",
        description = "Retrieves all transactions where the account was the sender"
    )
    @GetMapping("/account/{accountId}/sent")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getSentTransactions(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.debug("Fetching sent transactions for account: {}", accountId);
        
        List<TransactionResponse> transactions = transactionService.getSentTransactions(accountId);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @Operation(
        summary = "Get received (credit) transactions",
        description = "Retrieves all transactions where the account was the receiver"
    )
    @GetMapping("/account/{accountId}/received")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getReceivedTransactions(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.debug("Fetching received transactions for account: {}", accountId);
        
        List<TransactionResponse> transactions = transactionService.getReceivedTransactions(accountId);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @Operation(
        summary = "Get transactions by date range",
        description = "Retrieves transactions for an account within the specified date range"
    )
    @GetMapping("/account/{accountId}/range")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByDateRange(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId,
            @Parameter(description = "Start date (ISO format)", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Fetching transactions for account {} between {} and {}", accountId, startDate, endDate);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(accountId, startDate, endDate);
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @Operation(
        summary = "Get recent transactions",
        description = "Retrieves the last 10 transactions across all accounts"
    )
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getRecentTransactions() {
        
        log.debug("Fetching recent transactions");
        
        List<TransactionResponse> transactions = transactionService.getRecentTransactions();
        
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}
