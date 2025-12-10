package com.example.tests;

import com.example.config.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Exists endpoint: GET /api/v1/accounts/{accountId}/exists
 * 
 * Tests cover:
 * - Check existing accounts
 * - Check non-existing accounts
 * - Check inactive/suspended/closed accounts
 * - Response structure validation
 */
@DisplayName("Account Exists Tests - GET /api/v1/accounts/{accountId}/exists")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountExistsTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";
    private static final String EXISTS_SUFFIX = "/exists";

    // ==================== EXISTING ACCOUNT TESTS ====================

    @Test
    @Order(1)
    @DisplayName("TC001: Check existing account A001 - should return true")
    void testExistingAccountA001() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(true));
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("TC002: Check all sample accounts exist")
    @ValueSource(strings = {"A001", "A002", "A003", "A004", "A005", "A006", "A007"})
    void testAllSampleAccountsExist(String accountId) {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + accountId + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(true));
    }

    @Test
    @Order(3)
    @DisplayName("TC003: Check inactive account exists - A006")
    void testInactiveAccountExists() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A006" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(true));  // Inactive accounts still exist
    }

    @Test
    @Order(4)
    @DisplayName("TC004: Check suspended account exists - A007")
    void testSuspendedAccountExists() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A007" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(true));  // Suspended accounts still exist
    }

    // ==================== NON-EXISTING ACCOUNT TESTS ====================

    @Test
    @Order(10)
    @DisplayName("TC010: Check non-existing account - should return false")
    void testNonExistingAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/NOTEXIST" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(false));
    }

    @ParameterizedTest
    @Order(11)
    @DisplayName("TC011: Check various non-existing account IDs")
    @ValueSource(strings = {"XYZ999", "INVALID", "AAAA", "Z999", "TEST0000"})
    void testVariousNonExistingAccounts(String accountId) {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/" + accountId + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", equalTo(false));
    }

    @Test
    @Order(12)
    @DisplayName("TC012: Check with random UUID-like string - should return false")
    void testWithRandomUuid() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/ABC123DEF456" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data", equalTo(false));
    }

    // ==================== RESPONSE STRUCTURE TESTS ====================

    @Test
    @Order(20)
    @DisplayName("TC020: Verify response structure for existing account")
    void testResponseStructureForExistingAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", notNullValue())
            .body("success", instanceOf(Boolean.class))
            .body("data", notNullValue())
            .body("data", instanceOf(Boolean.class))
            .body("timestamp", notNullValue());
    }

    @Test
    @Order(21)
    @DisplayName("TC021: Verify response structure for non-existing account")
    void testResponseStructureForNonExistingAccount() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/NOTEXIST" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", notNullValue())
            .body("success", instanceOf(Boolean.class))
            .body("data", notNullValue())
            .body("data", instanceOf(Boolean.class));
    }

    // ==================== CASE SENSITIVITY TESTS ====================

    @Test
    @Order(30)
    @DisplayName("TC030: Check with lowercase account ID - case sensitivity test")
    void testWithLowercaseAccountId() {
        // A001 exists, but a001 might not depending on DB case sensitivity
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/a001" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
            // data could be true or false depending on DB configuration
    }

    @Test
    @Order(31)
    @DisplayName("TC031: Check with mixed case account ID")
    void testWithMixedCaseAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A0o1" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data", equalTo(false));  // Mixed case should not match
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @Order(40)
    @DisplayName("TC040: Check with minimum length account ID")
    void testWithMinimumLengthAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/AAAA" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test
    @Order(41)
    @DisplayName("TC041: Check with maximum length account ID")
    void testWithMaximumLengthAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/AAAAAAAAAAAA" + EXISTS_SUFFIX)  // 12 chars
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test
    @Order(42)
    @DisplayName("TC042: Check with numeric only account ID")
    void testWithNumericOnlyAccountId() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/1234567890" + EXISTS_SUFFIX)
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("data", equalTo(false));
    }

    // ==================== PERFORMANCE TESTS ====================

    @Test
    @Order(50)
    @DisplayName("TC050: Exists check should be fast (under 1 second)")
    void testExistsCheckPerformance() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001" + EXISTS_SUFFIX)
        .then()
            .time(lessThan(1000L));  // Should respond in less than 1 second
    }

    @Test
    @Order(51)
    @DisplayName("TC051: Non-existing account check should also be fast")
    void testNonExistingCheckPerformance() {
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/NOTEXIST" + EXISTS_SUFFIX)
        .then()
            .time(lessThan(1000L));
    }

    // ==================== MULTIPLE RAPID CHECKS ====================

    @Test
    @Order(60)
    @DisplayName("TC060: Multiple rapid exists checks")
    void testMultipleRapidExistsChecks() {
        String[] accountIds = {"A001", "A002", "A003", "NOTEXIST", "A004", "XYZ", "A005"};
        boolean[] expectedResults = {true, true, true, false, true, false, true};

        for (int i = 0; i < accountIds.length; i++) {
            final int index = i;
            given()
                .spec(requestSpec)
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + accountIds[index] + EXISTS_SUFFIX)
            .then()
                .statusCode(200)
                .body("data", equalTo(expectedResults[index]));
        }
    }

    // ==================== SPECIAL CHARACTERS IN ACCOUNT ID ====================

    @Test
    @Order(70)
    @DisplayName("TC070: Check with special characters in account ID")
    void testWithSpecialCharactersInAccountId() {
        // URL encoding might be needed for special characters
        given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001%20" + EXISTS_SUFFIX)  // A001 with space
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(400), equalTo(404)));
    }
}
