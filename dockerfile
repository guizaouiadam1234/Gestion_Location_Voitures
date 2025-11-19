# Build stage with JDK 25
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Install Maven manually
RUN apt-get update && apt-get install -y maven

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# -------------------------
# Run stage (also Java 25)
# -------------------------
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy compiled jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
