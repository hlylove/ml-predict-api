FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

COPY src/main/resources/certs/ ./certs/
COPY models/ ./models/

EXPOSE 443

ENTRYPOINT ["java", "-jar", "app.jar"]