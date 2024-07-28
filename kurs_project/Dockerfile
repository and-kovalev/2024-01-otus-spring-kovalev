FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY /target/kurs_project-1.0.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]