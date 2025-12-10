package com.example.tests;

import com.example.config.BaseTest;
import com.example.pojo.AccountRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Creation endpoint: POST /api/v1/accounts
 * 
 * Tests cover:
 * - Happy path scenarios
 * - Validation error scenarios
 * - Duplicate account scenarios
 * - Boundary conditions
 */
@DisplayName("Account Creation Tests - POST /api/v1/accounts")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountCreateTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";

    // ==================== HAPPY PATH TESTS ====================

    @Test
    @Order(1)
    @DisplayName("TC001: Create account with all valid fields")
    void testCreateAccountWithAllFields() {
        String uniqueId = generateUniqueAccountId();
        
        AccountRequest request = AccountRequest.builder()
                .accountId(uniqueId)
                .accountHolderName("John Doe")
                .email(generateUniqueEmail())
                .phone(generateUniquePhone())
                .initialBalance(new BigDecimal("5000.00"))
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
            .body("message", equalTo("Account created successfully"))
            .body("data.accountId", equalTo(uniqueId))
            .body("data.accountHolderName", equalTo("John Doe"))
            .body("data.balance", equalTo(5000.00f))
            .body("data.accountType", equalTo("SAVINGS"))
            .body("data.status", equalTo("ACTIVE"))
            .body("data.createdAt", notNullValue())
            .body("data.updatedAt", notNullValue());
    }

    @Test
    @Order(2)
    @DisplayName("TC002: Create account with minimum required fields")
    void testCreateAccountWithMinimumFields() {
        String uniqueId = generateUniqueAccountId();
        
        AccountRequest request = AccountRequest.builder()
                .accountId(uniqueId)
                .accountHolderName("Minimal User")
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
            .body("data.accountId", equalTo(uniqueId))
            .body("data.status", equalTo("ACTIVE"));
    }

    @Test
    @Order(3)
    @DisplayName("TC003: Create account with zero initial balance")
    void testCreateAccountWithZeroBalance() {
        String uniqueId = generateUniqueAccountId();
        
        AccountRequest request = AccountRequest.builder()
                .accountId(uniqueId)
                .accountHolderName("Zero Balance User")
                .initialBalance(BigDecimal.ZERO)
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
            .body("data.balance", equalTo(0.0f));
    }

    // ==================== ACCOUNT TYPE TESTS ====================

    @ParameterizedTest
    @Order(4)
    @DisplayName("TC004: Create account with different account types")
    @ValueSource(strings = {"SAVINGS", "CURRENT", "SALARY"})
    void testCreateAccountWithDifferentTypes(String accountType) {
        String uniqueId = generateUniqueAccountId();
        
        AccountRequest request = AccountRequest.builder()
                .accountId(uniqueId)
                .accountHolderName("Type Test User")
                .accountType(accountType)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("data.accountType", equalTo(accountType));
    }

    // ==================== VALIDATION ERROR TESTS ====================

    @Test
    @Order(10)
    @DisplayName("TC010: Create account without account ID - should fail")
    void testCreateAccountWithoutAccountId() {
        AccountRequest request = AccountRequest.builder()
                .accountHolderName("No ID User")
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("VALIDATION_ERROR"));
    }

    @Test
    @Order(11)
    @DisplayName("TC011: Create account without account holder name - should fail")
    void testCreateAccountWithoutHolderName() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("VALIDATION_ERROR"));
    }

    @Test
    @Order(12)
    @DisplayName("TC012: Create account with empty account ID - should fail")
    void testCreateAccountWithEmptyAccountId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("")
                .accountHolderName("Empty ID User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(13)
    @DisplayName("TC013: Create account with invalid email format - should fail")
    void testCreateAccountWithInvalidEmail() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Invalid Email User")
                .email("invalid-email")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("VALIDATION_ERROR"));
    }

    @ParameterizedTest
    @Order(14)
    @DisplayName("TC014: Create account with invalid phone format - should fail")
    @ValueSource(strings = {"12345", "12345678901234", "abcdefghij", "98765-43210"})
    void testCreateAccountWithInvalidPhone(String invalidPhone) {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Invalid Phone User")
                .phone(invalidPhone)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(15)
    @DisplayName("TC015: Create account with negative initial balance - should fail")
    void testCreateAccountWithNegativeBalance() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Negative Balance User")
                .initialBalance(new BigDecimal("-100.00"))
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    // ==================== DUPLICATE ACCOUNT TESTS ====================

    @Test
    @Order(20)
    @DisplayName("TC020: Create account with duplicate account ID - should fail")
    void testCreateAccountWithDuplicateId() {
        // Use existing account ID from sample data
        AccountRequest request = AccountRequest.builder()
                .accountId("A001")  // Existing account
                .accountHolderName("Duplicate ID User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(409)
            .body("success", equalTo(false))
            .body("error.code", equalTo("DUPLICATE_ACCOUNT"));
    }

    @Test
    @Order(21)
    @DisplayName("TC021: Create account with duplicate email - should fail")
    void testCreateAccountWithDuplicateEmail() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Duplicate Email User")
                .email("john.doe@example.com")  // Existing email from sample data
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(409)
            .body("success", equalTo(false));
    }

    @Test
    @Order(22)
    @DisplayName("TC022: Create account with duplicate phone - should fail")
    void testCreateAccountWithDuplicatePhone() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Duplicate Phone User")
                .phone("9876543210")  // Existing phone from sample data
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(409)
            .body("success", equalTo(false));
    }

    // ==================== BOUNDARY TESTS ====================

    @Test
    @Order(30)
    @DisplayName("TC030: Create account with minimum length account ID (4 chars)")
    void testCreateAccountWithMinLengthId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("T001")  // 4 characters - minimum
                .accountHolderName("Min ID User")
                .build();

        Response response = given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT);

        // Either 201 (created) or 409 (already exists) is acceptable
        int statusCode = response.getStatusCode();
        Assertions.assertTrue(statusCode == 201 || statusCode == 409,
                "Expected 201 or 409, got " + statusCode);
    }

    @Test
    @Order(31)
    @DisplayName("TC031: Create account with maximum length account ID (12 chars)")
    void testCreateAccountWithMaxLengthId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("TESTMAX12345")  // 12 characters - maximum
                .accountHolderName("Max ID User")
                .build();

        Response response = given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT);

        int statusCode = response.getStatusCode();
        Assertions.assertTrue(statusCode == 201 || statusCode == 409,
                "Expected 201 or 409, got " + statusCode);
    }

    @Test
    @Order(32)
    @DisplayName("TC032: Create account with account ID too short (3 chars) - should fail")
    void testCreateAccountWithTooShortId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("T01")  // 3 characters - too short
                .accountHolderName("Short ID User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(33)
    @DisplayName("TC033: Create account with account ID too long (13 chars) - should fail")
    void testCreateAccountWithTooLongId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("TESTMAXLONG13")  // 13 characters - too long
                .accountHolderName("Long ID User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(34)
    @DisplayName("TC034: Create account with very long holder name (100 chars)")
    void testCreateAccountWithLongHolderName() {
        String longName = "A".repeat(100);  // Maximum allowed
        
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName(longName)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("success", equalTo(true));
    }

    @Test
    @Order(35)
    @DisplayName("TC035: Create account with holder name too long (101 chars) - should fail")
    void testCreateAccountWithTooLongHolderName() {
        String tooLongName = "A".repeat(101);  // Exceeds maximum
        
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName(tooLongName)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(36)
    @DisplayName("TC036: Create account with large initial balance")
    void testCreateAccountWithLargeBalance() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("Rich User")
                .initialBalance(new BigDecimal("9999999999999.99"))
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(201)
            .body("success", equalTo(true));
    }

    // ==================== SPECIAL CHARACTER TESTS ====================

    @Test
    @Order(40)
    @DisplayName("TC040: Create account with special characters in holder name")
    void testCreateAccountWithSpecialCharsInName() {
        AccountRequest request = AccountRequest.builder()
                .accountId(generateUniqueAccountId())
                .accountHolderName("John O'Brien-Smith Jr.")
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
            .body("data.accountHolderName", equalTo("John O'Brien-Smith Jr."));
    }

    @Test
    @Order(41)
    @DisplayName("TC041: Create account with lowercase account ID - should fail")
    void testCreateAccountWithLowercaseId() {
        AccountRequest request = AccountRequest.builder()
                .accountId("test123")  // lowercase - should fail
                .accountHolderName("Lowercase ID User")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    // ==================== EMPTY REQUEST BODY TEST ====================

    @Test
    @Order(50)
    @DisplayName("TC050: Create account with empty request body - should fail")
    void testCreateAccountWithEmptyBody() {
        given()
            .spec(requestSpec)
            .body("{}")
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(51)
    @DisplayName("TC051: Create account with invalid JSON - should fail")
    void testCreateAccountWithInvalidJson() {
        given()
            .spec(requestSpec)
            .body("{ invalid json }")
        .when()
            .post(ACCOUNTS_ENDPOINT)
        .then()
            .statusCode(400);
    }
}
