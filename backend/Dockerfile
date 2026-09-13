FROM gradle:jdk25 AS build
WORKDIR /src
COPY . .
RUN gradle --no-daemon clean bootJar

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
