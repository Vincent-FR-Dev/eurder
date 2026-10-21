package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.*;
import com.switchfully.eurder.security.SecurityConfig;
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

    private static final String PASSWORD = "password123";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static String postCustomer() {
        String email = "customer." + UUID.randomUUID() + "@switchfully.com";
        AddressDto address = new AddressDto("Rue des loups", "2", "1000", "Bruxelles");
        CreateCustomerDto customer = new CreateCustomerDto("Rudy", "Switchfully", email, address, "0472548963", PASSWORD);
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/customers")
                .then()
                .statusCode(201);
        return email;
    }

    private static UUID postItem(String price, int stock) {
        CreateItemDto item = new CreateItemDto("VTT", "Sport Bike", new BigDecimal(price), stock);
        String id = given()
                .auth().preemptive().basic(SecurityConfig.ADMIN_EMAIL, SecurityConfig.ADMIN_PASSWORD)
                .contentType(ContentType.JSON)
                .body(item)
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    private static CreateEurderDto eurderWith(UUID itemId, int quantity) {
        return new CreateEurderDto(List.of(new CreateItemGroupDto(itemId, quantity)));
    }

    private static UUID postEurder(String customerEmail, UUID itemId) {
        String id = given()
                .auth().preemptive().basic(customerEmail, PASSWORD)
                .contentType(ContentType.JSON)
                .body(eurderWith(itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(id);
    }

    @Test
    void whenEurderIsValid_returns201() {
        String email = postCustomer();
        UUID itemId = postItem("350.00", 5);

        double totalPrice = given()
                .auth().preemptive().basic(email, PASSWORD)
                .contentType(ContentType.JSON)
                .body(eurderWith(itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(201)
                .extract().jsonPath().getDouble("totalPrice");

        assertThat(totalPrice).isEqualTo(700.0);
    }

    @Test
    void whenNotAuthenticated_returns401() {
        UUID itemId = postItem("350.00", 5);

        given()
                .contentType(ContentType.JSON)
                .body(eurderWith(itemId, 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(401);
    }

    @Test
    void whenItemIsUnknown_returns404() {
        String email = postCustomer();

        given()
                .auth().preemptive().basic(email, PASSWORD)
                .contentType(ContentType.JSON)
                .body(eurderWith(UUID.randomUUID(), 2))
                .when()
                .post("/eurders")
                .then()
                .statusCode(404);
    }

    @Test
    void whenEurderIsEmpty_returns400() {
        String email = postCustomer();

        given()
                .auth().preemptive().basic(email, PASSWORD)
                .contentType(ContentType.JSON)
                .body(new CreateEurderDto(List.of()))
                .when()
                .post("/eurders")
                .then()
                .statusCode(400);
    }

    @Test
    void whenReorderIsValid_returns201() {
        String email = postCustomer();
        UUID itemId = postItem("350.00", 5);
        UUID eurderId = postEurder(email, itemId);

        given()
                .auth().preemptive().basic(email, PASSWORD)
                .when()
                .post("/eurders/" + eurderId + "/reorder")
                .then()
                .statusCode(201)
                .body("id", not(equalTo(eurderId.toString())));
    }

    @Test
    void whenReorderingUnknownEurder_returns404() {
        String email = postCustomer();

        given()
                .auth().preemptive().basic(email, PASSWORD)
                .when()
                .post("/eurders/" + UUID.randomUUID() + "/reorder")
                .then()
                .statusCode(404);
    }

    @Test
    void whenReorderingEurderOfAnotherCustomer_returns403() {
        String ownerEmail = postCustomer();
        String otherEmail = postCustomer();
        UUID itemId = postItem("350.00", 5);
        UUID eurderId = postEurder(ownerEmail, itemId);

        given()
                .auth().preemptive().basic(otherEmail, PASSWORD)
                .when()
                .post("/eurders/" + eurderId + "/reorder")
                .then()
                .statusCode(403);
    }
}