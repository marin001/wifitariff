
# Backend Bewerber Task  - wifiapi

This is a Spring Boot application using Java 21, managed with Maven.

**Task:**
Create a CRUD webservice that manages WIFI tariffs.
A tariff is an entity detailing WIFI functionality a customer can buy, such as bandwidth, online duration time, and prices.

## Initial Setup

> **Note:** Maven (MVN) must be installed before running or building this project. See the [official Maven installation guide](https://maven.apache.org/install.html) for instructions.

- **Spring Boot Starter**: Core Spring Boot functionality.
- **Spring Boot Starter Test**: For unit and integration testing.
- **Maven Wrapper**: Ensures consistent Maven usage across environments.
- **application.properties**: Main configuration file for the application.

### Project Dependencies

This project uses the following main dependencies:

- **Jakarta Validation API (`jakarta.validation-api`)**: Provides standard validation annotations (e.g., `@NotBlank`, `@NotNull`) for model validation.
- **Hibernate Validator**: The reference implementation for Jakarta Bean Validation, used by Spring Boot to enforce validation annotations at runtime.

These dependencies ensure the application is robust, testable, and follows best practices for data validation and maintainability.

## Used Developer Tools
- **Checkstyle**: Enforces code style and formatting.
- **PMD**: Detects common programming flaws.
- **SpotBugs**: Static analysis for potential bugs.
- **Spotless**: Ensures consistent code formatting and style.

All tools are configured as Maven plugins and can be run with:
- `mvn checkstyle:check`
- `mvn pmd:check`
- `mvn spotbugs:check`
- `mvn spotless:check`
- `mvn spotless:apply`

## Testing

### Usage

To run the test suite and verify the application:

```bash
mvn test
```

This will execute all unit and integration tests, including validation and edge cases for the WiFi tariff API.

### Coverage
	- Basic CRUD operations for tariffs (create, read, update, delete)
	- Edge and error cases (invalid input, missing fields, non-existent IDs, etc.)
	- Validation of request data using annotation-based validation

Test cases are located in `src/test/java/com/m3connect/wifiapi/controller/TariffControllerTest.java` and use MockMvc to simulate HTTP requests and verify API responses.

### API Documentation (Swagger UI)

This project includes Swagger UI for easy API exploration and testing.

- After starting the application, open your browser and go to:

	[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- You can view, try out, and test all available endpoints directly from the web interface.


## Running in Docker
To build and run in Docker:
```bash
docker build -t wifiapi .
docker run -p 8080:8080 wifiapi
```


The application will be available on port 8080 by default.
