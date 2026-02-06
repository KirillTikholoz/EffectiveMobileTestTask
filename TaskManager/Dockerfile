FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml /app
RUN mvn dependency:go-offline -B

COPY src /app/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/TaskManager.jar /app/task_manger.jar
ENTRYPOINT ["java", "-jar", "task_manger.jar"]