# Stage 1: Build the Maven project using OpenJDK 16
FROM maven:3.8.1-openjdk-16 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven configuration file and source code
COPY pom.xml .
COPY src ./src

# Build the Maven project, including running unit tests
RUN mvn clean install -DskipTests=false

# Stage 2: Create the final image
FROM flink:1.17

# Set the working directory
WORKDIR /opt/app

# Copy the JAR file from the build stage
COPY --from=build /app/target/flink-services.jar /opt/flink/userlib/app.jar

# Ensure proper permissions
RUN chmod +x /opt/flink/userlib/app.jar

USER flink

WORKDIR /opt/flink