package com.demo.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseApiTest {

    @BeforeAll
    static void setup() {
        String baseUrl =
                System.getProperty("baseUrl",
                        System.getenv().getOrDefault("BASE_URL", "http://localhost:8080"));

        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
