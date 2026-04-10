# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

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
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/financedb
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=your_password
ENV JWT_SECRET=super_secret_key_that_is_at_least_256_bits_long_for_security_reasons
ENV JWT_EXPIRATION=86400000

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
