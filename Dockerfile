# ----------------------------
# Stage 1: Build the Application
# ----------------------------
# Use the Maven image based on Eclipse Temurin (Valid Replacement for OpenJDK)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ----------------------------
# Stage 2: Run the Application
# ----------------------------
# Use Eclipse Temurin JRE (Runtime only, lighter and secure)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR file from the build stage
# Ensure this name matches your pom.xml artifactId + version
COPY --from=build /app/target/movie-reservation-system-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]