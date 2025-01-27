# Use a base image with JDK installed
FROM openjdk:23-jdk-bullseye AS build

# Set working directory
WORKDIR /app

# Copy local files to the container
COPY . .

# Make the gradlew file executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build
# run the build without running the tests
# RUN ./gradlew build -x test

# Use a base image with JDK installed
FROM openjdk:23-jdk-bullseye

# Set working directory
WORKDIR /app

# Copy the built application from the build image
COPY --from=build /app/build/libs/*SNAPSHOT.jar brainquest.jar

# Expose the port your app runs on
EXPOSE 8080

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "brainquest.jar"]