# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

# Grant execution rights to the maven wrapper
RUN chmod +x ./mvnw

# Package the application (skip tests for faster deployment, but ideally run them in CI)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the generated JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set optimal Java memory limits for containers
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Default environment variables (override these at runtime)
ENV PORT=8080
ENV DB_URL=jdbc:postgresql://localhost:5432/financedb
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=your_password
ENV JWT_SECRET=super_secret_key_that_is_at_least_256_bits_long_for_security_reasons
ENV JWT_EXPIRATION_MS=86400000

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
