# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies to leverage Docker caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root system user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built jar from the build stage
COPY --from=build --chown=spring:spring /app/target/bfhl-1.0.0.jar app.jar

# Expose server port (8080 default)
EXPOSE 8080

# Run JVM with optimized container-aware settings
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]
