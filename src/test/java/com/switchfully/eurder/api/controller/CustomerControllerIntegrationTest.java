package com.switchfully.eurder.api.controller;

import com.switchfully.eurder.api.dto.AddressDto;
import com.switchfully.eurder.api.dto.CreateCustomerDto;
import com.switchfully.eurder.security.SecurityConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static CreateCustomerDto customerWithEmail(String email) {
        AddressDto address = new AddressDto("Rue des loups", "2", "1000", "Bruxelles");
        return new CreateCustomerDto("Rudy", "Switchfully", email, address, "0472548963", "password123");
    }

    private static String postCustomer(String email) {
        return given()
                .contentType(ContentType.JSON)
                .body(customerWithEmail(email))
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .extract().path("id");
    }


    @Test
    void createCustomer_withValidData_returns201(){
        given().contentType(ContentType.JSON)
                .body(customerWithEmail("rudy@switchfully.com"))
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void createCustomer_withInvalidEmail_returns400(){
        given().contentType(ContentType.JSON)
                .body(customerWithEmail("bonjour"))
                .when()
                .post("/customers")
                .then()
                .statusCode(400);
    }

    @Test
    void createCustomer_withEmptyEmail_returns400(){
        given().contentType(ContentType.JSON)
                .body(customerWithEmail(""))
                .when()
                .post("/customers")
                .then()
                .statusCode(400);
    }

    @Test
    void getAllCustomers_returns200(){
        postCustomer("rudy@switchfully.com");
        postCustomer("karel@switchfully.com");

        given().auth().preemptive().basic(SecurityConfig.ADMIN_EMAIL, SecurityConfig.ADMIN_PASSWORD)
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void getCustomerById_whenCustomerExists_returns200(){
        String id = postCustomer("rudy@switchfully.com");

        given().auth().preemptive().basic(SecurityConfig.ADMIN_EMAIL, SecurityConfig.ADMIN_PASSWORD)
                .when()
                .get("/customers/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id));

    }

    @Test
    void getCustomerById_whenCustomerDoesNotExist_returns404(){
        given().auth().preemptive().basic(SecurityConfig.ADMIN_EMAIL, SecurityConfig.ADMIN_PASSWORD)
                .when()
                .get("/customers/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

}