FROM openjdk:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app

COPY ./mvnw .
COPY pom.xml .
RUN mvn clean install

COPY target/table-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
