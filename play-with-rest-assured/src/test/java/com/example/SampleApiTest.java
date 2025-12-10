package com.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class SampleApiTest extends BaseTest {


    @Test
    void sampleTest() {

         given()
                .spec(reqSpec)
                .queryParam("type", "ADMIN")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                 .body(matchesJsonSchemaInClasspath("user_list_schema.json"));


    }

}
