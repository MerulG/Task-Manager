# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (better caching)
COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Install Postgres client for pg_isready
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

# Copy the packaged JAR
COPY --from=build /app/target/*.jar app.jar

# Copy wait-for script
COPY wait-for-postgres.sh .
RUN chmod +x wait-for-postgres.sh

EXPOSE 8080

# Use the wait-for script as entrypoint
ENTRYPOINT ["./wait-for-postgres.sh", "db", "java", "-jar", "app.jar"]
