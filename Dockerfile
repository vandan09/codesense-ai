# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-24-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the built application
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/codereview-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
