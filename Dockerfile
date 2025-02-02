FROM openjdk:22

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

COPY target/incode-exercise.jar app.jar
RUN chown -R appuser:appgroup /app

EXPOSE 8080

USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
