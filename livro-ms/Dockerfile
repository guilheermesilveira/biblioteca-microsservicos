FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY autor-ms/pom.xml autor-ms/pom.xml
COPY livro-ms/pom.xml livro-ms/pom.xml
COPY front-ms/pom.xml front-ms/pom.xml
COPY livro-ms/src livro-ms/src
RUN mvn -pl livro-ms -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/livro-ms/target/livro-ms-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
