# Backend Bewerber Task  - wifiapi

This is a Spring Boot application using Java 21, managed with Maven.

**Task:**  
Create a CRUD webservice that manages WIFI tariffs.  
A tariff is an entity detailing WIFI functionality a customer can buy, such as bandwidth, online duration time, and prices.

## Initial Setup
- **Spring Boot Starter**: Core Spring Boot functionality.
- **Spring Boot Starter Test**: For unit and integration testing.
- **Maven Wrapper**: Ensures consistent Maven usage across environments.
- **application.properties**: Main configuration file for the application.

## Used Developer Tools
- **Checkstyle**: Enforces code style and formatting.
- **PMD**: Detects common programming flaws.
- **SpotBugs**: Static analysis for potential bugs.


All tools are configured as Maven plugins and can be run with:
- `mvn checkstyle:check`
- `mvn pmd:check`
- `mvn spotbugs:check`
- `mvn spotless:check` – Checks code formatting using Spotless.
- `mvn spotless:apply` – Automatically formats code according to the rules.

## Running in Docker
To build and run in Docker:
```bash
docker build -t wifiapi .
docker run -p 8080:8080 wifiapi
```

The application will be available on port 8080 by default.
