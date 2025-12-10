package com.example.tests;

import com.example.config.BaseTest;
import com.example.pojo.AccountRequest;
import com.example.pojo.AccountUpdateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Account Management - End-to-End scenarios.
 * 
 * Tests complete workflows involving multiple API calls:
 * - Create -> Read -> Update -> Delete lifecycle
 * - Data consistency across operations
 * - State transitions
 */
@DisplayName("Account Integration Tests - End-to-End Scenarios")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountIntegrationTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";
    
    // Shared test data
    private static String integrationTestAccountId;
    private static String integrationTestEmail;
    private static String integrationTestPhone;

    @BeforeAll
    static void setupTestData() {
        long timestamp = System.currentTimeMillis();
        integrationTestAccountId = "INT" + timestamp % 100000;
        integrationTestEmail = "integration." + timestamp + "@example.com";
        integrationTestPhone = "9111" + String.format("%06d", timestamp % 1000000);
    }

    // ==================== FULL LIFECYCLE TEST ====================

    @Test
    @Order(1)
    @DisplayName("E2E-001: Create new account")
    void testCreateNewAccount() {
        AccountRequest request = AccountRequest.builder()
                .accountId(integrationTestAccountId)
                .accountHolderName("Integration Test User")
                .email(integrationTestEmail)
                .phone(integrationTestPhone)
                .initialBalance(new BigDecimal("10000.00"))
                .accountType("SAVINGS")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(integrationTestAccountId))
            .body("data.accountHolderName", equalTo("Integration Test User"))
            .body("data.email", equalTo(integrationTestEmail))
            .body("data.phone", equalTo(integrationTestPhone))
            .body("data.balance", equalTo(10000.00f))
            .body("data.accountType", equalTo("SAVINGS"))
            .body("data.status", equalTo("ACTIVE"));
    }

    @Test
    @Order(2)
    @DisplayName("E2E-002: Verify account exists")
    void testVerifyAccountExists() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId + "/exists")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data", equalTo(true));
    }

    @Test
    @Order(3)
    @DisplayName("E2E-003: Read created account")
    void testReadCreatedAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(integrationTestAccountId))
            .body("data.accountHolderName", equalTo("Integration Test User"))
            .body("data.status", equalTo("ACTIVE"));
    }

    @Test
    @Order(4)
    @DisplayName("E2E-004: Verify account appears in list")
    void testAccountAppearsInList() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.accountId", hasItem(integrationTestAccountId));
    }

    @Test
    @Order(5)
    @DisplayName("E2E-005: Update account holder name")
    void testUpdateAccountHolderName() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Integration Test User Updated")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountHolderName", equalTo("Integration Test User Updated"));
    }

    @Test
    @Order(6)
    @DisplayName("E2E-006: Verify update persisted")
    void testVerifyUpdatePersisted() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.accountHolderName", equalTo("Integration Test User Updated"));
    }

    @Test
    @Order(7)
    @DisplayName("E2E-007: Update account status to INACTIVE")
    void testUpdateStatusToInactive() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status("INACTIVE")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.status", equalTo("INACTIVE"));
    }

    @Test
    @Order(8)
    @DisplayName("E2E-008: Verify account appears in INACTIVE filter")
    void testAccountAppearsInInactiveFilter() {
        given()
            .spec(requestSpec)
            .queryParam("status", "INACTIVE")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.accountId", hasItem(integrationTestAccountId));
    }

    @Test
    @Order(9)
    @DisplayName("E2E-009: Verify account does NOT appear in ACTIVE filter")
    void testAccountNotInActiveFilter() {
        given()
            .spec(requestSpec)
            .queryParam("status", "ACTIVE")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.accountId", not(hasItem(integrationTestAccountId)));
    }

    @Test
    @Order(10)
    @DisplayName("E2E-010: Reactivate account")
    void testReactivateAccount() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status("ACTIVE")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.status", equalTo("ACTIVE"));
    }

    @Test
    @Order(11)
    @DisplayName("E2E-011: Delete (close) account")
    void testDeleteAccount() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("message", equalTo("Account closed successfully"));
    }

    @Test
    @Order(12)
    @DisplayName("E2E-012: Verify account status is CLOSED after delete")
    void testVerifyAccountClosed() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.status", equalTo("CLOSED"));
    }

    @Test
    @Order(13)
    @DisplayName("E2E-013: Verify account still exists (soft delete)")
    void testAccountStillExistsAfterDelete() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId + "/exists")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data", equalTo(true));
    }

    // ==================== DATA CONSISTENCY TESTS ====================

    @Test
    @Order(20)
    @DisplayName("E2E-020: Data consistency - balance unchanged after updates")
    void testBalanceUnchangedAfterUpdates() {
        // Get original balance
        Response response = given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .extract().response();

        Number originalBalance = response.path("data.balance");

        // Update holder name
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Balance Test User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId);

        // Verify balance unchanged
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + integrationTestAccountId)
        .then()
            .body("data.balance", equalTo(originalBalance.floatValue()));
    }

    // ==================== CONCURRENT-LIKE OPERATIONS ====================

    @Test
    @Order(30)
    @DisplayName("E2E-030: Multiple operations on same account")
    void testMultipleOperationsOnSameAccount() {
        // Create a new account for this test
        String testId = "MULT" + System.currentTimeMillis() % 10000;
        String testEmail = "multi." + System.currentTimeMillis() + "@example.com";

        // Create
        AccountRequest createRequest = AccountRequest.builder()
                .accountId(testId)
                .accountHolderName("Multi Op User")
                .email(testEmail)
                .initialBalance(new BigDecimal("5000.00"))
                .build();

        given()
            .spec(requestSpec)
            .body(createRequest)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(201);

        // Update 1
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().accountHolderName("Multi Op User 1").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200);

        // Update 2
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().accountHolderName("Multi Op User 2").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200);

        // Update 3
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().accountHolderName("Multi Op User Final").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200);

        // Verify final state
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200)
            .body("data.accountHolderName", equalTo("Multi Op User Final"))
            .body("data.balance", equalTo(5000.00f));  // Balance should be unchanged

        // Cleanup
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testId);
    }

    // ==================== BULK OPERATIONS ====================

    @Test
    @Order(40)
    @DisplayName("E2E-040: Create multiple accounts and verify")
    void testCreateMultipleAccountsAndVerify() {
        String[] accountIds = new String[3];
        
        // Create 3 accounts
        for (int i = 0; i < 3; i++) {
            accountIds[i] = "BULK" + System.currentTimeMillis() % 10000 + i;
            
            AccountRequest request = AccountRequest.builder()
                    .accountId(accountIds[i])
                    .accountHolderName("Bulk User " + i)
                    .email("bulk" + i + "." + System.currentTimeMillis() + "@example.com")
                    .initialBalance(new BigDecimal((i + 1) * 1000))
                    .build();

            given()
                .spec(requestSpec)
                .body(request)
            .when()
                .post(ACCOUNTS_ENDPOINT)
            .then()
                .statusCode(201);
        }

        // Verify all accounts exist
        for (String accountId : accountIds) {
            given()
                .spec(requestSpec)
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + accountId + "/exists")
            .then()
                .statusCode(200)
                .body("data", equalTo(true));
        }

        // Cleanup - close all accounts
        for (String accountId : accountIds) {
            given()
                .spec(requestSpec)
            .when()
                .delete(ACCOUNTS_ENDPOINT + "/" + accountId);
        }
    }

    // ==================== STATE TRANSITION TESTS ====================

    @Test
    @Order(50)
    @DisplayName("E2E-050: State transitions - ACTIVE -> SUSPENDED -> ACTIVE -> CLOSED")
    void testStateTransitions() {
        String testId = "STATE" + System.currentTimeMillis() % 10000;

        // Create active account
        AccountRequest createRequest = AccountRequest.builder()
                .accountId(testId)
                .accountHolderName("State Transition User")
                .email("state." + System.currentTimeMillis() + "@example.com")
                .build();

        given()
            .spec(requestSpec)
            .body(createRequest)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(201)
            .body("data.status", equalTo("ACTIVE"));

        // ACTIVE -> SUSPENDED
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().status("SUSPENDED").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200)
            .body("data.status", equalTo("SUSPENDED"));

        // SUSPENDED -> ACTIVE
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().status("ACTIVE").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200)
            .body("data.status", equalTo("ACTIVE"));

        // ACTIVE -> CLOSED (via delete)
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200);

        // Verify final state
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200)
            .body("data.status", equalTo("CLOSED"));
    }

    // ==================== ERROR RECOVERY TESTS ====================

    @Test
    @Order(60)
    @DisplayName("E2E-060: Operations after failed validation should work")
    void testOperationsAfterFailedValidation() {
        String testId = "RECOV" + System.currentTimeMillis() % 10000;

        // Create account
        AccountRequest createRequest = AccountRequest.builder()
                .accountId(testId)
                .accountHolderName("Recovery Test User")
                .email("recovery." + System.currentTimeMillis() + "@example.com")
                .build();

        given()
            .spec(requestSpec)
            .body(createRequest)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(201);

        // Attempt invalid update (should fail)
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().email("invalid-email").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(400);

        // Valid update should still work
        given()
            .spec(requestSpec)
            .body(AccountUpdateRequest.builder().accountHolderName("Recovery Updated").build())
        .when()
            .put(ACCOUNTS_ENDPOINT + "/" + testId)
        .then()
            .statusCode(200)
            .body("data.accountHolderName", equalTo("Recovery Updated"));

        // Cleanup
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testId);
    }
}
