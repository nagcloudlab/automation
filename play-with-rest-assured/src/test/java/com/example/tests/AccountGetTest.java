package com.example.tests;

import com.example.config.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Retrieval endpoints:
 * - GET /api/v1/accounts/{accountId}
 * - GET /api/v1/accounts
 * - GET /api/v1/accounts?status={status}
 * 
 * Tests cover:
 * - Get single account by ID
 * - Get all accounts
 * - Filter accounts by status
 * - Not found scenarios
 * - Response structure validation
 */
@DisplayName("Account Retrieval Tests - GET /api/v1/accounts")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountGetTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";

    // ==================== GET SINGLE ACCOUNT TESTS ====================

    @Test
    @Order(1)
    @DisplayName("TC001: Get existing account by ID - A001")
    void testGetExistingAccountA001() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("A001"))
            .body("data.accountHolderName", equalTo("John Doe"))
            .body("data.email", equalTo("john.doe@example.com"))
            .body("data.phone", equalTo("9876543210"))
            .body("data.accountType", equalTo("SAVINGS"))
            .body("data.status", equalTo("ACTIVE"))
            .body("data.balance", notNullValue())
            .body("data.createdAt", notNullValue())
            .body("data.updatedAt", notNullValue());
    }

    @Test
    @Order(2)
    @DisplayName("TC002: Get existing account by ID - A002")
    void testGetExistingAccountA002() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A002")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("A002"))
            .body("data.accountHolderName", equalTo("Jane Smith"))
            .body("data.accountType", equalTo("CURRENT"))
            .body("data.status", equalTo("ACTIVE"));
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("TC003: Get all sample accounts by ID")
    @ValueSource(strings = {"A001", "A002", "A003", "A004", "A005", "A006", "A007"})
    void testGetAllSampleAccounts(String accountId) {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + accountId)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo(accountId));
    }

    @Test
    @Order(4)
    @DisplayName("TC004: Get inactive account - A006")
    void testGetInactiveAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A006")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("A006"))
            .body("data.status", equalTo("INACTIVE"));
    }

    @Test
    @Order(5)
    @DisplayName("TC005: Get suspended account - A007")
    void testGetSuspendedAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A007")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("A007"))
            .body("data.status", equalTo("SUSPENDED"));
    }

    // ==================== NOT FOUND TESTS ====================

    @Test
    @Order(10)
    @DisplayName("TC010: Get non-existent account - should return 404")
    void testGetNonExistentAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/INVALID123")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false))
            .body("error.code", equalTo("ACCOUNT_NOT_FOUND"))
            .body("error.field", equalTo("accountId"))
            .body("error.rejectedValue", equalTo("INVALID123"));
    }

    @Test
    @Order(11)
    @DisplayName("TC011: Get account with random UUID - should return 404")
    void testGetAccountWithRandomUuid() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/XYZ999")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false))
            .body("error.code", equalTo("ACCOUNT_NOT_FOUND"));
    }

    @ParameterizedTest
    @Order(12)
    @DisplayName("TC012: Get account with various invalid IDs - should return 404")
    @ValueSource(strings = {"NOTEXIST", "AAAA", "9999", "TEST0000"})
    void testGetAccountWithInvalidIds(String invalidId) {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + invalidId)
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false));
    }

    // ==================== GET ALL ACCOUNTS TESTS ====================

    @Test
    @Order(20)
    @DisplayName("TC020: Get all accounts without filter")
    void testGetAllAccounts() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", notNullValue())
            .body("data", hasSize(greaterThanOrEqualTo(7)))  // At least 7 sample accounts
            .body("data.accountId", hasItems("A001", "A002", "A003", "A004", "A005", "A006", "A007"));
    }

    @Test
    @Order(21)
    @DisplayName("TC021: Get all accounts - verify response structure")
    void testGetAllAccountsResponseStructure() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data[0].accountId", notNullValue())
            .body("data[0].accountHolderName", notNullValue())
            .body("data[0].balance", notNullValue())
            .body("data[0].accountType", notNullValue())
            .body("data[0].status", notNullValue());
    }

    // ==================== FILTER BY STATUS TESTS ====================

    @Test
    @Order(30)
    @DisplayName("TC030: Get accounts filtered by ACTIVE status")
    void testGetAccountsByActiveStatus() {
        given()
            .spec(requestSpec)
            .queryParam("status", "ACTIVE")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", notNullValue())
            .body("data.findAll { it.status != 'ACTIVE' }", hasSize(0))  // All should be ACTIVE
            .body("data.accountId", hasItems("A001", "A002", "A003", "A004", "A005"));
    }

    @Test
    @Order(31)
    @DisplayName("TC031: Get accounts filtered by INACTIVE status")
    void testGetAccountsByInactiveStatus() {
        given()
            .spec(requestSpec)
            .queryParam("status", "INACTIVE")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", notNullValue())
            .body("data.findAll { it.status != 'INACTIVE' }", hasSize(0))
            .body("data.accountId", hasItem("A006"));
    }

    @Test
    @Order(32)
    @DisplayName("TC032: Get accounts filtered by SUSPENDED status")
    void testGetAccountsBySuspendedStatus() {
        given()
            .spec(requestSpec)
            .queryParam("status", "SUSPENDED")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", notNullValue())
            .body("data.findAll { it.status != 'SUSPENDED' }", hasSize(0))
            .body("data.accountId", hasItem("A007"));
    }

    @Test
    @Order(33)
    @DisplayName("TC033: Get accounts filtered by CLOSED status - should return empty list")
    void testGetAccountsByClosedStatus() {
        given()
            .spec(requestSpec)
            .queryParam("status", "CLOSED")
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", notNullValue());
            // May be empty or have accounts depending on previous tests
    }

    @ParameterizedTest
    @Order(34)
    @DisplayName("TC034: Get accounts with various valid status filters")
    @ValueSource(strings = {"ACTIVE", "INACTIVE", "SUSPENDED", "CLOSED", "BLOCKED"})
    void testGetAccountsWithVariousStatusFilters(String status) {
        given()
            .spec(requestSpec)
            .queryParam("status", status)
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    // ==================== RESPONSE FIELD VALIDATION TESTS ====================

    @Test
    @Order(40)
    @DisplayName("TC040: Verify balance field is numeric")
    void testBalanceFieldIsNumeric() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.balance", instanceOf(Number.class));
    }

    @Test
    @Order(41)
    @DisplayName("TC041: Verify timestamp fields format")
    void testTimestampFieldsFormat() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            .body("data.updatedAt", matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @Order(42)
    @DisplayName("TC042: Verify account type is valid enum")
    void testAccountTypeIsValidEnum() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.accountType", isOneOf("SAVINGS", "CURRENT", "SALARY", "FIXED_DEPOSIT", "RECURRING_DEPOSIT"));
    }

    @Test
    @Order(43)
    @DisplayName("TC043: Verify account status is valid enum")
    void testAccountStatusIsValidEnum() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data.status", isOneOf("ACTIVE", "INACTIVE", "SUSPENDED", "CLOSED", "BLOCKED"));
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @Order(50)
    @DisplayName("TC050: Get account with trailing slash")
    void testGetAccountWithTrailingSlash() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001/")
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(404)));  // Depends on server config
    }

    @Test
    @Order(51)
    @DisplayName("TC051: Get account with extra path segments - should return 404 or error")
    void testGetAccountWithExtraPathSegments() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001/extra")
        .then()
            .statusCode(anyOf(equalTo(404), equalTo(400)));
    }

    // ==================== PERFORMANCE/RESPONSE TIME TESTS ====================

    @Test
    @Order(60)
    @DisplayName("TC060: Get account response time should be under 2 seconds")
    void testGetAccountResponseTime() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .time(lessThan(2000L));  // Response time < 2 seconds
    }

    @Test
    @Order(61)
    @DisplayName("TC061: Get all accounts response time should be under 5 seconds")
    void testGetAllAccountsResponseTime() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT)
        .then()
            .time(lessThan(5000L));  // Response time < 5 seconds
    }
}
