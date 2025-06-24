# Stage 1: Build with Maven using JDK 24
FROM eclipse-temurin:24-jdk-alpine AS build

# Install Maven manually
RUN apk add --no-cache curl tar bash && \
    curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz | tar -xz -C /opt && \
    ln -s /opt/apache-maven-3.9.6 /opt/maven

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the built JAR with JDK 24
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/codereview-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
