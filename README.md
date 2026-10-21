# Eurder

Back-end REST API (JSON) for **Eurder**, an order management application, built during the Switchfully Java training.

Customers can create an account and order items; admins can add and update items and view customers.

## Tech stack

- Java 17
- Spring Boot 4 (Web MVC, Validation, Security)
- Maven
- springdoc-openapi (Swagger UI)
- JUnit 5, AssertJ, RestAssured

## Running the application

```bash
./mvnw spring-boot:run
```

Swagger UI is then available at: **http://localhost:8080/swagger-ui.html**

Protected endpoints ask for credentials in the browser (see [Security](#security)).

## Running the tests

```bash
./mvnw test
```

The project contains unit tests (domain, repositories, services) and integration tests (one class per controller, using RestAssured on a random port, with authentication).

## Implemented stories

| Story | Description | Endpoint | Access |
|---|---|---|---|
| 0 | Project setup, REST API, Swagger, error logging | — | — |
| 1 | Create a customer account | `POST /customers` | anyone |
| 2 | Add an item | `POST /items` | admin |
| 3 | Order items | `POST /eurders` | customer |
| 4 | Update an item | `PUT /items/{id}` | admin |
| 6 | Reorder an existing order | `POST /eurders/{id}/reorder` | customer |
| 7 | View all customers | `GET /customers` | admin |
| 8 | View a single customer | `GET /customers/{id}` | admin |

### Not implemented

- Stories 5 (order report), 9 (items shipping today) and 10 (stock resupply urgency)
- Jenkins CI pipeline

## Security

Spring Security with HTTP Basic authentication (stateless). Passwords are hashed with BCrypt.

| Role | Credentials | Access |
|---|---|---|
| Anyone | none | `POST /customers`, Swagger |
| Customer | email + password chosen at account creation | `POST /eurders`, `POST /eurders/{id}/reorder` |
| Admin | predefined: `admin@eurder.com` / `admin123` | `/items`, `GET /customers`, `GET /customers/{id}` |

- The customer placing or reordering an order is taken from the authenticated user, not from the request body.
- Missing or invalid credentials return **401**; a forbidden action returns **403**. Both are logged.
- The admin account is predefined because no story allows creating one. In a real application, its credentials would not be stored in the code.

## Design choices

### Layered architecture

```
api         → controllers, DTOs, mappers, global exception handler
service     → business logic
domain      → Customer, Address, Item, Eurder, ItemGroup, Role
repository  → data storage
exception   → custom business exceptions
security    → Spring Security configuration
```

DTOs and mapping stay in the `api` layer: controllers convert DTOs to domain objects before calling the services, so the service layer never depends on the API. When the service needs input that cannot be built as a domain object yet (the requested item id and quantity of an order line), it receives a small service-level record, `ItemGroupRequest`.

### In-memory storage

No database was used (JPA had not been covered yet in the training). Each repository stores its entities in a `Map<UUID, ...>`. Data is lost when the application restarts.

### Orders (Story 3)

- An `Eurder` contains one or more `ItemGroup`s (one line per item, with a quantity).
- **The item price is copied into the `ItemGroup` at order time**, instead of keeping a reference to the `Item`. If the price of an item changes later, existing orders keep the price that was actually paid.
- The shipping date is calculated when the `ItemGroup` is created: the next day if the stock covers the ordered quantity, otherwise one week later.
- Every ordered item must exist.
- Money amounts use `BigDecimal` to avoid rounding errors.
- Stock is not decreased when an order is placed (not required by the assignment).

### Reorder (Story 6)

- A reorder creates a **new** order (new id) with the same items and quantities.
- The **current** item price is used, and the shipping date is recalculated from the current stock.
- A customer can only reorder their own orders: reordering another customer's order returns **403 Forbidden**.

### Error handling and logging

A `@RestControllerAdvice` (`GlobalExceptionHandler`) converts exceptions into HTTP responses with an `ErrorDto(status, message)` and logs every error. Security errors (401 / 403) are produced before the controllers are reached, so they are logged and formatted in `SecurityConfig`.

| Situation | Status | Log level |
|---|---|---|
| Unknown customer, item or order | 404 | WARN |
| Missing or invalid credentials | 401 | WARN |
| Forbidden action (wrong role, another customer's order) | 403 | WARN |
| Invalid input (validation) or malformed request | 400 | WARN |
| Unexpected error | 500 | ERROR (with stack trace, not returned to the client) |

Note: the API uses `eurder` instead of `order`, which is a reserved keyword in many contexts (e.g. SQL).
