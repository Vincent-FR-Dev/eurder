# Eurder

Back-end REST API (JSON) for **Eurder**, an order management application, built during the Switchfully Java training.

Customers can create an account and order items; admins can add and update items and view customers.

## Tech stack

- Java 17
- Spring Boot 4 (Web MVC, Validation)
- Maven
- springdoc-openapi (Swagger UI)
- JUnit 5, AssertJ, RestAssured

## Running the application

```bash
./mvnw spring-boot:run
```

Swagger UI is then available at: **http://localhost:8080/swagger-ui.html**

## Running the tests

```bash
./mvnw test
```

The project contains unit tests (domain, repositories, services) and integration tests (one class per controller, using RestAssured on a random port).

## Implemented stories

| Story | Description | Endpoint |
|---|---|---|
| 0 | Project setup, REST API, Swagger, error logging | — |
| 1 | Create a customer account | `POST /customers` |
| 2 | Add an item | `POST /items` |
| 3 | Order items | `POST /eurders` |
| 4 | Update an item | `PUT /items/{id}` |
| 6 | Reorder an existing order | `POST /eurders/{id}/reorder` |
| 7 | View all customers | `GET /customers` |
| 8 | View a single customer | `GET /customers/{id}` |

### Not implemented

- Stories 5 (order report), 9 (items shipping today) and 10 (stock resupply urgency)
- Security (authentication / authorization): as allowed by the assignment, admin-only endpoints are not protected
- Jenkins CI pipeline

## Design choices

### Layered architecture

```
api         → controllers, DTOs, mappers, global exception handler
service     → business logic
domain      → Customer, Address, Item, Eurder, ItemGroup
repository  → data storage
exception   → custom business exceptions
```

DTOs and mapping stay in the `api` layer: controllers convert DTOs to domain objects before calling the services, so the service layer never depends on the API. When the service needs input that cannot be built as a domain object yet (the requested item id and quantity of an order line), it receives a small service-level record, `ItemGroupRequest`.

### In-memory storage

No database was used (JPA had not been covered yet in the training). Each repository stores its entities in a `Map<UUID, ...>`. Data is lost when the application restarts.

### Orders (Story 3)

- An `Eurder` contains one or more `ItemGroup`s (one line per item, with a quantity).
- **The item price is copied into the `ItemGroup` at order time**, instead of keeping a reference to the `Item`. If the price of an item changes later, existing orders keep the price that was actually paid.
- The shipping date is calculated when the `ItemGroup` is created: the next day if the stock covers the ordered quantity, otherwise one week later.
- The order must belong to an existing customer; every item must exist.
- Money amounts use `BigDecimal` to avoid rounding errors.
- Stock is not decreased when an order is placed (not required by the assignment).

### Reorder (Story 6)

- A reorder creates a **new** order (new id) with the same items and quantities.
- The **current** item price is used, and the shipping date is recalculated from the current stock.
- A customer can only reorder their own orders: reordering another customer's order returns **403 Forbidden**.
- Without authentication, the customer identifies themselves with a `customerId` in the request body.

### Error handling and logging

A `@RestControllerAdvice` (`GlobalExceptionHandler`) converts exceptions into HTTP responses with an `ErrorDto(status, message)` and logs every error:

| Situation | Status | Log level |
|---|---|---|
| Unknown customer, item or order | 404 | WARN |
| Reordering another customer's order | 403 | WARN |
| Invalid input (validation) or malformed request | 400 | WARN |
| Unexpected error | 500 | ERROR (with stack trace, not returned to the client) |

Note: the API uses `eurder` instead of `order`, which is a reserved keyword in many contexts (e.g. SQL).
