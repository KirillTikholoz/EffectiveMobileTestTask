FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn clean install

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/TaskManager.jar /app/task_manger.jar
ENTRYPOINT ["java", "-jar", "task_manger.jar"]