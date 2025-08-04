FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/application.properties .

CMD ["java", "-jar", "app.jar", "--spring.config.location=classpath:/application.properties,file:./application.properties"]
