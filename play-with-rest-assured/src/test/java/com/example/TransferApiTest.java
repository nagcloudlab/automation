package com.example;

import com.example.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

public class TransferApiTest {

    @ParameterizedTest
    @CsvSource({
            "A001, A002, 1000",
            "A003, A004, 2500",
            "A005, A006, 750"
    })
    public void testTransfer(String fromAccId, String toAccId, int amount) {
        String transferRequestJson = """
                {
                  "fromAccId": "A001",
                  "toAccId": "A002",
                  "amount": 500,
                  "description": "Payment for services"
                }
                """;

//        TransferRequest transferRequest = new TransferRequest();
//        transferRequest.setFromAccId("A001");
//        transferRequest.setToAccId("A002");
//        transferRequest.setAmount(500);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccId(fromAccId);
        transferRequest.setToAccId(toAccId);
        transferRequest.setAmount(amount);

        given()
                .baseUri("http://localhost:8080")
                .basePath("/api/v1/transfers")
                .header("Content-Type", "application/json")
                .body(transferRequest)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

}
