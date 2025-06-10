# start with a maven image include jdk 21
FROM maven:3.9.9-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 21

FROM amazoncorretto:21.0.1-al2023


WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
