# Use an official lightweight OpenJDK 17 image
FROM eclipse-temurin:17-jdk-alpine

# Create and use app directory
WORKDIR /app

# Copy the JAR file into the image
COPY target/codereview-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
