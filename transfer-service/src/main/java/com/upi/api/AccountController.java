package com.upi.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upi.dto.AccountRequest;
import com.upi.dto.AccountResponse;
import com.upi.dto.AccountUpdateRequest;
import com.upi.dto.ApiResponse;
import com.upi.model.AccountStatus;
import com.upi.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for account management operations.
 * Provides CRUD endpoints for bank accounts.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "Create a new account",
        description = "Creates a new bank account with the provided details"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409", 
            description = "Account already exists")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody AccountRequest request) {
        
        log.info("Creating account: {}", request.getAccountId());
        
        AccountResponse response = accountService.createAccount(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully", response));
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves account details for the specified account ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Account found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Account not found")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.debug("Fetching account: {}", accountId);
        
        AccountResponse response = accountService.getAccount(accountId);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
        summary = "Get all accounts",
        description = "Retrieves all accounts, optionally filtered by status"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts(
            @Parameter(description = "Filter by account status", example = "ACTIVE")
            @RequestParam(required = false) AccountStatus status) {
        
        log.debug("Fetching accounts with status filter: {}", status);
        
        List<AccountResponse> accounts;
        if (status != null) {
            accounts = accountService.getAccountsByStatus(status);
        } else {
            accounts = accountService.getAllAccounts();
        }
        
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @Operation(
        summary = "Update account",
        description = "Updates account details for the specified account ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Account updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Account not found")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId,
            @Valid @RequestBody AccountUpdateRequest request) {
        
        log.info("Updating account: {}", accountId);
        
        AccountResponse response = accountService.updateAccount(accountId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", response));
    }

    @Operation(
        summary = "Delete (close) account",
        description = "Soft deletes an account by setting its status to CLOSED"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Account closed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Account not found")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.info("Deleting account: {}", accountId);
        
        accountService.deleteAccount(accountId);
        
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully"));
    }

    @Operation(
        summary = "Check if account exists",
        description = "Returns true if account exists, false otherwise"
    )
    @GetMapping("/{accountId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkAccountExists(
            @Parameter(description = "Account ID", example = "A001")
            @PathVariable String accountId) {
        
        log.debug("Checking if account exists: {}", accountId);
        
        boolean exists = accountService.accountExists(accountId);
        
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
