package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

public class AccountApiTest {

    @ParameterizedTest
    @ValueSource(strings = {"A001", "A002", "A003"})
    public void testGetAccountExist(String accountId) {
        given()
                .baseUri("http://localhost:8080")
                .basePath("/api/v1/accounts")
                .pathParam("accountId", accountId)
                .when()
                .get("/{accountId}")
                .then()
                .statusCode(200);
    }

}
