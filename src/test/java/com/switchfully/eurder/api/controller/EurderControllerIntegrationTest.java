package com.switchfully.eurder.api.controller;


import com.switchfully.eurder.api.dto.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EurderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static UUID postCustomer() {
        AddressDto address = new AddressDto("Rue des loups", "2", "1000", "Bruxelles");
        CreateCustomerDto customer = new CreateCustomerDto("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963");
        String id = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    private static UUID postItem(String price, int stock) {
        CreateItemDto item = new CreateItemDto("VTT", "Sport Bike", new BigDecimal(price), stock);
        String id = given()
                .contentType(ContentType.JSON)
                .body(item)
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    private static UUID postEurder(UUID customerId, UUID itemId) {
        String id = given()
                .contentType(ContentType.JSON)
                .body(eurderWith(customerId, itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    private static CreateEurderDto eurderWith(UUID customerId, UUID itemId, int quantity) {
        return new CreateEurderDto(customerId, List.of(new CreateItemGroupDto(itemId, quantity)));
    }

    @Test
    void whenEurderIsValid_returns201(){
        UUID customerId = postCustomer();
        UUID itemId = postItem("350.00", 5);

        double totalPrice = given().contentType(ContentType.JSON)
                .body(eurderWith(customerId, itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(201)
                .extract().jsonPath().getDouble("totalPrice");

        assertThat(totalPrice).isEqualTo(700.00);
    }

    @Test
    void whenCustomerIsUnknown_returns404(){
        UUID customerId = UUID.randomUUID();
        UUID itemId = postItem("350.00", 5);

        given().contentType(ContentType.JSON)
                .body(eurderWith(customerId, itemId, 4))
                .when()
                .post("/eurders")
                .then()
                .statusCode(404);

    }

    @Test
    void whenItemIsUnknown_returns404(){
        UUID customerId = postCustomer();
        UUID itemId = UUID.randomUUID();

        given().contentType(ContentType.JSON)
                .body(eurderWith(customerId, itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(404);
    }

    @Test
    void whenEurderIsEmpty_returns400(){
        UUID customerId = postCustomer();

        given().contentType(ContentType.JSON)
                .body(new CreateEurderDto(customerId, List.of()))
                .when()
                .post("/eurders")
                .then()
                .statusCode(400);
    }

    @Test
    void whenReorderIsValid_returns201(){
        UUID customerId = postCustomer();
        UUID itemId = postItem("350.00", 5);
        UUID eurderId = postEurder(customerId, itemId);

        given().contentType(ContentType.JSON)
                .body(new ReorderDto(customerId))
                .when()
                .post("/eurders/" + eurderId + "/reorder")
                .then()
                .statusCode(201)
                .body("id", not(equalTo(eurderId.toString())));
    }

    @Test
    void whenReorderWantToReorderAnUnknownEurder_returns404(){
        UUID customerId = postCustomer();

        given().contentType(ContentType.JSON)
                .body(new ReorderDto(customerId))
                .when()
                .post("/eurders/" + UUID.randomUUID() + "/reorder")
                .then()
                .statusCode(404);
    }

    @Test
    void whenReorderWantToReorderWithBadCustomerId_returns403(){
        UUID customerId = postCustomer();
        UUID itemId = postItem("350.00", 5);
        UUID eurderId = postEurder(customerId, itemId);

        given().contentType(ContentType.JSON)
                .body(new ReorderDto(UUID.randomUUID()))
                .when()
                .post("/eurders/" + eurderId + "/reorder")
                .then()
                .statusCode(403);
    }
}
