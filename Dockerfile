
# Stage 1: Build the application JAR
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Copy Maven or Gradle wrapper and dependency files first for layer caching
COPY pom.xml .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# Copy source code and package the JAR (skipping tests here or run them in CI)
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for better container security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Pass JVM and Spring properties via environment variables at runtime
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]