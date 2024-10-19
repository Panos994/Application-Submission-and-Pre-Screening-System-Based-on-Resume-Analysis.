

#FROM openjdk:17-jdk-slim
#COPY ./target/demo-0.0.1-SNAPSHOT.jar /app.jar
#WORKDIR /app
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Use the OpenJDK base image
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

# Expose the backend port (9090)
EXPOSE 9090

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]


