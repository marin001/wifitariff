# Use Maven to build the application

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Run code quality checks before packaging
RUN mvn checkstyle:check pmd:check spotbugs:check
RUN mvn clean package -DskipTests

# Use a lightweight JRE to run the app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Rename the built JAR to wifiapi.jar
COPY --from=build /app/target/wifiapi-0.0.1-SNAPSHOT.jar wifiapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "wifiapi.jar"]
