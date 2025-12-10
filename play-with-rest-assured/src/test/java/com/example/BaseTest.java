package com.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static RequestSpecification reqSpec;

    @BeforeAll
    static void setup() {
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8080)
                .setBasePath("/api/v1")
                .setContentType("application/json")
                .build();
    }

}
