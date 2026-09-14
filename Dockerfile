# Stage 1: build the jar using the Gradle wrapper (no need for Gradle preinstalled)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# Stage 2: run it with a lighter JRE-only image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Render assigns the actual port via the PORT env var (already handled
# in application.yml via server.port: ${PORT:8080})
ENTRYPOINT ["java", "-jar", "app.jar"]
