package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.CreateItemDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static CreateItemDto itemWithPrice(BigDecimal price) {
        return new CreateItemDto("Computer", "Gaming computer nvidia", price, 15);
    }

    private static UUID postItem() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(itemWithPrice(new BigDecimal("350.00")))
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    @Test
    void createItem_withValidPrice_returns201() {
        given().contentType(ContentType.JSON)
                .body(itemWithPrice(new BigDecimal("1000.00")))
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void createItem_withInvalidPrice_returns400() {
        given().contentType(ContentType.JSON)
                .body(itemWithPrice(new BigDecimal("0")))
                .when()
                .post("/items")
                .then()
                .statusCode(400);
    }

    @Test
    void updateItem_withValidData_returns200() {
        UUID id = postItem();

        double price = given().contentType(ContentType.JSON)
                .body(itemWithPrice(new BigDecimal("500.00")))
                .when()
                .put("/items/" + id)
                .then()
                .statusCode(200)
                .extract().jsonPath().getDouble("price");

        assertThat(price).isEqualTo(500.0);
    }

    @Test
    void updateItem_withUnknownId_returns404() {
        given().contentType(ContentType.JSON)
                .body(itemWithPrice(new BigDecimal("500.00")))
                .when()
                .put("/items/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }
}