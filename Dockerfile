FROM eclipse-temurin:17.0.8.1_1-jdk AS builder

# Set the working directory
WORKDIR /app

# Copy the Gradle files and source code
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Give execution permission to Gradle wrapper and build the project
RUN apt-get update && apt-get install dos2unix
RUN dos2unix gradlew
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# ---

# Start a new stage for running the application
FROM eclipse-temurin:17.0.8.1_1-jdk

# Set the working directory
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]