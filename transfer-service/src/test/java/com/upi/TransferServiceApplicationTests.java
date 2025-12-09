package com.upi;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.upi.dto.AccountResponse;
import com.upi.dto.ApiResponse;
import com.upi.dto.TransferRequest;
import com.upi.dto.TransferResponse;
import com.upi.service.AccountService;

/**
 * Integration tests for Transfer Service Application.
 * These tests demonstrate basic API testing that students can expand upon
 * using REST Assured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
class TransferServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        assertThat(accountService).isNotNull();
    }

    @Test
    @DisplayName("GET /api/v1/accounts/A001 - Should return existing account")
    void testGetAccount() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/accounts/A001",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("A001");
        assertThat(response.getBody()).contains("John Doe");
    }

    @Test
    @DisplayName("GET /api/v1/accounts/INVALID - Should return 404")
    void testGetAccountNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/accounts/INVALID",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("ACCOUNT_NOT_FOUND");
    }

    @Test
    @DisplayName("GET /api/v1/accounts - Should return all accounts")
    void testGetAllAccounts() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/accounts",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("success");
    }

    @Test
    @DisplayName("POST /api/v1/transfers - Should transfer funds successfully")
    void testFundTransfer() {
        TransferRequest request = TransferRequest.builder()
                .fromAccId("A001")
                .toAccId("A002")
                .amount(new BigDecimal("100.00"))
                .description("Test transfer")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/transfers",
                request,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("SUCCESS");
    }

    @Test
    @DisplayName("GET /api/v1/transactions/recent - Should return recent transactions")
    void testGetRecentTransactions() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/transactions/recent",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("success");
    }

    @Test
    @DisplayName("GET /actuator/health - Should return health status")
    void testHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }
}
