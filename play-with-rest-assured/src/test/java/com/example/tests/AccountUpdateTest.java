package com.example.tests;

import com.example.config.BaseTest;
import com.example.pojo.AccountUpdateRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Account Update endpoint: PUT /api/v1/accounts/{accountId}
 * 
 * Tests cover:
 * - Update holder name
 * - Update email
 * - Update phone
 * - Update status
 * - Partial updates
 * - Validation errors
 * - Not found scenarios
 */
@DisplayName("Account Update Tests - PUT /api/v1/accounts/{accountId}")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountUpdateTest extends BaseTest {

    private static final String ACCOUNTS_ENDPOINT = "/accounts";

    // ==================== HAPPY PATH UPDATE TESTS ====================

    @Test
    @Order(1)
    @DisplayName("TC001: Update account holder name")
    void testUpdateAccountHolderName() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("John Doe Updated")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("message", equalTo("Account updated successfully"))
            .body("data.accountId", equalTo("A001"))
            .body("data.accountHolderName", equalTo("John Doe Updated"));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .accountHolderName("John Doe")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }

    @Test
    @Order(2)
    @DisplayName("TC002: Update account email")
    void testUpdateAccountEmail() {
        String newEmail = "john.updated." + System.currentTimeMillis() + "@example.com";
        
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .email(newEmail)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.email", equalTo(newEmail));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .email("john.doe@example.com")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }

    @Test
    @Order(3)
    @DisplayName("TC003: Update account phone")
    void testUpdateAccountPhone() {
        String newPhone = "9999" + String.format("%06d", System.currentTimeMillis() % 1000000);
        
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .phone(newPhone)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.phone", equalTo(newPhone));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .phone("9876543210")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }

    @Test
    @Order(4)
    @DisplayName("TC004: Update multiple fields at once")
    void testUpdateMultipleFields() {
        String uniqueEmail = "multi.update." + System.currentTimeMillis() + "@example.com";
        String uniquePhone = "9888" + String.format("%06d", System.currentTimeMillis() % 1000000);
        
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Multi Update User")
                .email(uniqueEmail)
                .phone(uniquePhone)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A003")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountHolderName", equalTo("Multi Update User"))
            .body("data.email", equalTo(uniqueEmail))
            .body("data.phone", equalTo(uniquePhone));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .accountHolderName("Alice Johnson")
                .email("alice.j@example.com")
                .phone("9876543212")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A003");
    }

    // ==================== STATUS UPDATE TESTS ====================

    @Test
    @Order(10)
    @DisplayName("TC010: Update account status to INACTIVE")
    void testUpdateStatusToInactive() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status("INACTIVE")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A004")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.status", equalTo("INACTIVE"));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .status("ACTIVE")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A004");
    }

    @Test
    @Order(11)
    @DisplayName("TC011: Update account status to SUSPENDED")
    void testUpdateStatusToSuspended() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status("SUSPENDED")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A005")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.status", equalTo("SUSPENDED"));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .status("ACTIVE")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A005");
    }

    @Test
    @Order(12)
    @DisplayName("TC012: Reactivate inactive account")
    void testReactivateInactiveAccount() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status("ACTIVE")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A006")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.status", equalTo("ACTIVE"));
        
        // Revert the change
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .status("INACTIVE")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A006");
    }

    @ParameterizedTest
    @Order(13)
    @DisplayName("TC013: Update account with all valid status values")
    @ValueSource(strings = {"ACTIVE", "INACTIVE", "SUSPENDED", "BLOCKED"})
    void testUpdateWithAllValidStatuses(String status) {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .status(status)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A002")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.status", equalTo(status));
        
        // Revert to ACTIVE
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .status("ACTIVE")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A002");
    }

    // ==================== VALIDATION ERROR TESTS ====================

    @Test
    @Order(20)
    @DisplayName("TC020: Update with invalid email format - should fail")
    void testUpdateWithInvalidEmail() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .email("invalid-email-format")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("VALIDATION_ERROR"));
    }

    @ParameterizedTest
    @Order(21)
    @DisplayName("TC021: Update with invalid phone formats - should fail")
    @ValueSource(strings = {"12345", "123456789012", "abcdefghij", "98765-43210"})
    void testUpdateWithInvalidPhone(String invalidPhone) {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .phone(invalidPhone)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(22)
    @DisplayName("TC022: Update with holder name too long - should fail")
    void testUpdateWithTooLongHolderName() {
        String tooLongName = "A".repeat(101);  // Exceeds 100 char limit
        
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName(tooLongName)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    @Test
    @Order(23)
    @DisplayName("TC023: Update with holder name too short - should fail")
    void testUpdateWithTooShortHolderName() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("A")  // Less than 2 char minimum
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(400)
            .body("success", equalTo(false));
    }

    // ==================== DUPLICATE VALUE TESTS ====================

    @Test
    @Order(30)
    @DisplayName("TC030: Update with duplicate email (existing in another account) - should fail")
    void testUpdateWithDuplicateEmail() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .email("jane.smith@example.com")  // A002's email
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")  // Try to update A001 with A002's email
        .then()
            .spec(responseSpec)
            .statusCode(409)
            .body("success", equalTo(false));
    }

    @Test
    @Order(31)
    @DisplayName("TC031: Update with duplicate phone (existing in another account) - should fail")
    void testUpdateWithDuplicatePhone() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .phone("9876543211")  // A002's phone
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")  // Try to update A001 with A002's phone
        .then()
            .spec(responseSpec)
            .statusCode(409)
            .body("success", equalTo(false));
    }

    @Test
    @Order(32)
    @DisplayName("TC032: Update email with same value (no actual change) - should succeed")
    void testUpdateWithSameEmail() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .email("john.doe@example.com")  // Same as A001's current email
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    // ==================== NOT FOUND TESTS ====================

    @Test
    @Order(40)
    @DisplayName("TC040: Update non-existent account - should return 404")
    void testUpdateNonExistentAccount() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Non Existent")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/NOTEXIST")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false))
            .body("error.code", equalTo("ACCOUNT_NOT_FOUND"));
    }

    @Test
    @Order(41)
    @DisplayName("TC041: Update with random account ID - should return 404")
    void testUpdateWithRandomAccountId() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Random Update")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/XYZ999")
        .then()
            .spec(responseSpec)
            .statusCode(404)
            .body("success", equalTo(false));
    }

    // ==================== EMPTY/NULL UPDATE TESTS ====================

    @Test
    @Order(50)
    @DisplayName("TC050: Update with empty request body - should succeed (no changes)")
    void testUpdateWithEmptyBody() {
        given()
            .spec(requestSpec)
            .body("{}")
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test
    @Order(51)
    @DisplayName("TC051: Update with null values - should succeed (ignores nulls)")
    void testUpdateWithNullValues() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName(null)
                .email(null)
                .phone(null)
                .status(null)
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true));
    }

    // ==================== SPECIAL CHARACTER TESTS ====================

    @Test
    @Order(60)
    @DisplayName("TC060: Update holder name with special characters")
    void testUpdateWithSpecialCharactersInName() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("John O'Brien-Smith Jr.")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountHolderName", equalTo("John O'Brien-Smith Jr."));
        
        // Revert
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .accountHolderName("John Doe")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }

    @Test
    @Order(61)
    @DisplayName("TC061: Update holder name with unicode characters")
    void testUpdateWithUnicodeCharacters() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("José García")
                .build();

        given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .spec(responseSpec)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountHolderName", equalTo("José García"));
        
        // Revert
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .accountHolderName("John Doe")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }

    // ==================== VERIFY UPDATED_AT TIMESTAMP ====================

    @Test
    @Order(70)
    @DisplayName("TC070: Verify updatedAt timestamp changes on update")
    void testUpdatedAtTimestampChanges() {
        // Get current updatedAt
        String originalUpdatedAt = given()
            .spec(requestSpec)
        .when()
            .get(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .extract().path("data.updatedAt");

        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        // Update the account
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountHolderName("Timestamp Test User")
                .build();

        String newUpdatedAt = given()
            .spec(requestSpec)
            .body(request)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001")
        .then()
            .extract().path("data.updatedAt");

        Assertions.assertNotEquals(originalUpdatedAt, newUpdatedAt,
                "updatedAt timestamp should change after update");
        
        // Revert
        AccountUpdateRequest revertRequest = AccountUpdateRequest.builder()
                .accountHolderName("John Doe")
                .build();
        
        given()
            .spec(requestSpec)
            .body(revertRequest)
        .when()
            .put(ACCOUNTS_ENDPOINT + "/A001");
    }
}
