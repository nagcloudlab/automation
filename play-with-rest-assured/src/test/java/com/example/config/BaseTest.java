package com.example.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base test configuration for REST Assured tests.
 * Sets up common configurations like base URI, port, and content type.
 * 
 * PREREQUISITE: Transfer Service must be running on localhost:8080
 * Start it with: mvn spring-boot:run (in transfer-service directory)
 */
public abstract class BaseTest {

    protected static final String BASE_URI = "http://localhost";
    protected static final int PORT = 8080;
    protected static final String BASE_PATH = "/api/v1";

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setupRestAssured() {
        // Configure REST Assured defaults
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = PORT;
        RestAssured.basePath = BASE_PATH;

        // Enable logging for debugging
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        // Build request specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setPort(PORT)
                .setBasePath(BASE_PATH)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        // Build response specification
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Helper method to generate unique account IDs for testing
     */
    protected String generateUniqueAccountId() {
        return "TEST" + System.currentTimeMillis() % 100000;
    }

    /**
     * Helper method to generate unique email for testing
     */
    protected String generateUniqueEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    /**
     * Helper method to generate unique phone for testing
     */
    protected String generateUniquePhone() {
        return "98" + String.format("%08d", System.currentTimeMillis() % 100000000);
    }
}
