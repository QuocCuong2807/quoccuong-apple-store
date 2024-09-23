FROM openjdk:17-alpine

WORKDIR /app
COPY ./target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/backend.jar"]