FROM eclipse-temurin:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} dockerpoc.jar

ENTRYPOINT ["java", "-jar", "dockerpoc.jar"]

ENV PORT 8080

EXPOSE 8080