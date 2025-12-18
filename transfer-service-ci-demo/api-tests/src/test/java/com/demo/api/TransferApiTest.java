package com.demo.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TransferApiTest extends BaseApiTest {

    @Test
    @DisplayName("Successful fund transfer")
    void shouldTransferSuccessfully() {

        String payload = """
                {
                  "fromAccount": "ACC1001",
                  "toAccount": "ACC2001",
                  "amount": 500
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("transactionId", notNullValue())
                .body("timestamp", notNullValue());
    }


    @Test
    @DisplayName("Transfer fails when amount is missing")
    void shouldFailWhenAmountMissing() {

        String payload = """
      {
        "fromAccount": "ACC1001",
        "toAccount": "ACC2001"
      }
      """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400);
    }

}