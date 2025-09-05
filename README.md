# Backend Bewerber Task - wifiapi

This project was created as a **backend developer candidate task** to demonstrate practical skills in building a robust Spring Boot REST API. It focuses on implementing a complete CRUD web service for managing WiFi tariffs, incorporating real-world features such as API key authentication, automated syncing from remote sources, and comprehensive testing.

The goal is to showcase the ability to:

- Design and implement RESTful endpoints with proper validation and error handling  
- Secure APIs using custom filters and API keys  
- Use Spring Boot’s scheduling capabilities for background tasks  
- Write meaningful unit and integration tests  
- Document APIs using Swagger UI  
- Prepare the application for containerized deployment with Docker  

This repository serves both as a coding exercise.

---


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


### API Authentication

This application secures all main API endpoints using an API key. You must provide the correct API key in the `X-API-KEY` header for all requests except for public endpoints (Swagger UI, OpenAPI docs, and dummy endpoints).

- The API key is configured in `src/main/resources/application.properties`:
	```properties
	api.key=7Xq9B2mLpZ
	```

- To use the API key in Swagger UI:
	1. Click the "Authorize" button in the Swagger UI interface.
	2. Enter your API key in the `X-API-KEY` field.
	3. Click "Authorize" to apply the key to all requests.

- For direct API calls (e.g., with curl):
	```bash
	curl -H "X-API-KEY: 7Xq9B2mLpZ" http://localhost:8080/tariffs
	```

### API Documentation (Swagger UI)

This project includes Swagger UI for easy API exploration and testing.

- After starting the application, open your browser and go to:

		[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- You can view, try out, and test all available endpoints directly from the web interface. Remember to authorize with your API key for protected endpoints.


## Tariff Syncing

This application supports automatic and manual syncing of tariffs from a remote source for demonstration and integration purposes.

- **Automatic Sync:**
	- The service periodically fetches tariffs from a dummy remote endpoint (`/dummy-remote-tariffs`) every 60 seconds and merges them into the local store.
	- Existing tariffs are updated if their `remoteId` matches; new tariffs are added with a new local ID and the remote ID stored for reference.

- **Endpoints for Sync Testing:**
	- `/dummy-remote-tariffs`: Returns a list of randomly generated tariffs simulating a remote source.
	- `/dummy-list-all-tariffs`: Returns all tariffs currently in the local store for inspection.

- **Configuration:**
	- Sync logic is implemented in `TariffSyncService.java`.
	- Scheduling is enabled via `@EnableScheduling` in the main application class.

## Running in Docker
To build and run in Docker:
```bash
docker build -t wifiapi .
docker run -p 8080:8080 wifiapi
```


The application will be available on port 8080 by default.


## Approach to the Problem & Logical Commitments

This project was developed iteratively, following a clear set of logical commitments aligned with the candidate task requirements and good software engineering principles:

### Initial Setup & Quality Tools

- Established a solid Spring Boot project foundation with Maven and Docker support.
- Integrated code quality tools including Checkstyle, PMD, SpotBugs, and Spotless to maintain clean and consistent code.  
  *(commit: Initial Commit)*

### CRUD API for WiFi Tariffs

- Developed a full CRUD REST API for managing WiFi tariffs with proper validation annotations on model fields (`@NotBlank`, `@NotNull`, `@NotEmpty`).
- Ensured all endpoints are covered with thorough tests simulating both normal and edge cases using MockMvc.  
  *(commit: FEAT: Add CRUD API WiFi tariffs with validation)*

### Swagger UI Integration

- Added Swagger UI with springdoc-openapi to provide automatic, interactive API documentation.
- Updated README to include instructions for API exploration and testing through the Swagger interface.  
  *(commit: FEAT: Add Swagger UI)*

### API Key Authentication

- Secured the API with a custom API key authentication filter, configurable via `application.properties`.
- Modified the Swagger UI configuration to support API key input and authorization.
- Covered API key validation and error scenarios extensively in tests.  
  *(commit: FEAT: add API key authentication)*

### Tariff Syncing & Shared Store

- Implemented `TariffSyncService` to synchronize tariffs from a dummy remote source both manually and on a schedule.
- Created dummy controllers (`DummyRemoteTariffsController` and `DummyListAllTariffsController`) to simulate remote data and allow inspection of synced tariffs.
- Introduced a shared `TariffStore` for consistent tariff data management across services and controllers.
- Updated authentication filter to exclude dummy endpoints from API key checks to facilitate testing.
- Refined `TariffController` to leverage the shared store and handle `remoteId`-based syncing updates.
- Enhanced test coverage with `AvailabilityEndpointsTests` and `TariffSyncServiceTests` to verify syncing behavior and API stability.
- Performed code refactoring and added detailed documentation for clarity and maintainability.  
  *(commit: feat: Implement tariff syncing)*
