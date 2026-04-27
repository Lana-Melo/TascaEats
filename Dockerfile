FROM maven:3.9.14-eclipse-temurin-17-alpine as builder
ADD pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

FROM docker.io/eclipse-temurin:17
VOLUME /tmp
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
COPY --from=builder /target/tascaeats-1.0.jar app/app.jar
#COPY --from=builder /target/site/jacoco /app/jacoco-report
EXPOSE 8080 8082
ENTRYPOINT ["java","-jar", "/app/app.jar"]
