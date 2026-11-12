FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml ./
COPY src src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/Bibliotheek-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=3000
EXPOSE 3000
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
