package com.upi.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upi.dto.ApiResponse;
import com.upi.dto.TransferRequest;
import com.upi.dto.TransferResponse;
import com.upi.service.TransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for fund transfer operations.
 * Provides endpoints for initiating and managing fund transfers.
 */
@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Fund Transfers", description = "APIs for transferring funds between accounts")
public class TransferController {

    private final TransferService transferService;

    @Operation(
        summary = "Initiate fund transfer",
        description = "Transfers funds from one account to another with validation"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Transfer completed successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid request or insufficient funds"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Account not found")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Transfer request details",
        required = true,
        content = @Content(
            schema = @Schema(implementation = TransferRequest.class),
            examples = @ExampleObject(
                name = "Sample Transfer",
                value = """
                    {
                        "fromAccId": "A001",
                        "toAccId": "A002",
                        "amount": 500.00,
                        "description": "Payment for services"
                    }
                    """
            )
        )
    )
    @PostMapping
    public ResponseEntity<ApiResponse<TransferResponse>> initiateTransfer(
            @Valid @RequestBody TransferRequest request) {
        
        log.info("Transfer request received: From={}, To={}, Amount={}",
                request.getFromAccId(), request.getToAccId(), request.getAmount());

        TransferResponse response = transferService.transferFunds(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transfer completed successfully", response));
    }
}
