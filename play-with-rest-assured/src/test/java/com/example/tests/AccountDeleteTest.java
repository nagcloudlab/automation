package com.example.tests;

import com.example.config.BaseTest;
import com.example.pojo.AccountRequest;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Delete endpoint: DELETE /api/v1/accounts/{accountId}
 * 
 * Note: Delete is a SOFT DELETE - it sets account status to CLOSED
 * 
 * Tests cover:
 * - Successful account deletion (soft delete)
 * - Delete already deleted account
 * - Delete non-existent account
 * - Verify account status after deletion
 */
@DisplayName("Account Delete Tests - DELETE /api/v1/accounts/{accountId}")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountDeleteTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";
    private static String testAccountId;

    @BeforeAll
    static void createTestAccount() {
        // Create a dedicated test account for delete tests
        testAccountId = "DEL" + System.currentTimeMillis() % 100000;
    }

    // ==================== SETUP - CREATE TEST ACCOUNT ====================

    @Test
    @Order(1)
    @DisplayName("Setup: Create test account for deletion tests")
    void setupCreateTestAccount() {
        AccountRequest request = AccountRequest.builder()
                .accountId(testAccountId)
                .accountHolderName("Delete Test User")
                .email("delete.test." + System.currentTimeMillis() + "@example.com")
                .initialBalance(new BigDecimal("1000.00"))
                .accountType("SAVINGS")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(201)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(testAccountId))
            .body("data.status", equalTo("ACTIVE"));
    }

    @Test
    @Order(2)
    @DisplayName("TC001: Verify test account exists before deletion")
    void testVerifyAccountExistsBeforeDelete() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(testAccountId))
            .body("data.status", equalTo("ACTIVE"));
    }

    // ==================== DELETE TESTS ====================

    @Test
    @Order(3)
    @DisplayName("TC002: Delete (close) existing account")
    void testDeleteExistingAccount() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("message", equalTo("Account closed successfully"));
    }

    @Test
    @Order(4)
    @DisplayName("TC003: Verify account status is CLOSED after deletion")
    void testVerifyAccountClosedAfterDelete() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(testAccountId))
            .body("data.status", equalTo("CLOSED"));
    }

    @Test
    @Order(5)
    @DisplayName("TC004: Delete already closed account - should still succeed")
    void testDeleteAlreadyClosedAccount() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test
    @Order(6)
    @DisplayName("TC005: Verify account still exists after multiple deletes (soft delete)")
    void testAccountStillExistsAfterDelete() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(testAccountId));
    }

    // ==================== NOT FOUND TESTS ====================

    @Test
    @Order(10)
    @DisplayName("TC010: Delete non-existent account - should return 404")
    void testDeleteNonExistentAccount() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/NOTEXIST")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false))
            .body("error.code", equalTo("ACCOUNT_NOT_FOUND"));
    }

    @Test
    @Order(11)
    @DisplayName("TC011: Delete with random account ID - should return 404")
    void testDeleteWithRandomAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/XYZ999")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false));
    }

    @Test
    @Order(12)
    @DisplayName("TC012: Delete with invalid format account ID - should return 404")
    void testDeleteWithInvalidFormatAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/INVALIDFORMAT123")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false));
    }

    // ==================== IDEMPOTENCY TESTS ====================

    @Test
    @Order(20)
    @DisplayName("TC020: Delete operation is idempotent - multiple deletes on same account")
    void testDeleteIdempotency() {
        // First delete
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .statusCode(200);

        // Second delete
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .statusCode(200);

        // Third delete
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .statusCode(200);

        // Verify status is still CLOSED
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + testAccountId)
        .then()
            .body("data.status", equalTo("CLOSED"));
    }

    // ==================== DELETE SAMPLE ACCOUNTS (USE WITH CAUTION) ====================

    @Test
    @Order(30)
    @DisplayName("TC030: Create and delete a new test account - full lifecycle")
    void testCreateAndDeleteFullLifecycle() {
        String lifecycleAccountId = "LIFE" + System.currentTimeMillis() % 10000;
        
        // Create
        AccountRequest createRequest = AccountRequest.builder()
                .accountId(lifecycleAccountId)
                .accountHolderName("Lifecycle Test User")
                .email("lifecycle." + System.currentTimeMillis() + "@example.com")
                .initialBalance(BigDecimal.ZERO)
                .build();

        given()
            .spec(requestSpec)
            .body(createRequest)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(201);

        // Verify created
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + lifecycleAccountId)
        .then()
            .statusCode(200)
            .body("data.status", equalTo("ACTIVE"));

        // Delete
        given()
            .spec(requestSpec)
        .when()
            .delete(ACCOUNTS_ENDPOINT + "/" + lifecycleAccountId)
        .then()
            .statusCode(200);

        // Verify closed
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + lifecycleAccountId)
        .then()
            .statusCode(200)
            .body("data.status", equalTo("CLOSED"));
    }

    // ==================== CLOSED ACCOUNTS IN FILTER ====================

    @Test
    @Order(40)
    @DisplayName("TC040: Closed accounts appear in CLOSED status filter")
    void testClosedAccountsInFilter() {
        given()
            .spec(requestSpec)
            .queryParam("status", "CLOSED")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.findAll { it.status == 'CLOSED' }.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(41)
    @DisplayName("TC041: Closed accounts do NOT appear in ACTIVE status filter")
    void testClosedAccountsNotInActiveFilter() {
        given()
            .spec(requestSpec)
            .queryParam("status", "ACTIVE")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", not(hasItem(testAccountId)));
    }
}
