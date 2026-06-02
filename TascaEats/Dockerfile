FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

COPY Common/ Common/
COPY TascaEats/ TascaEats/

RUN mvn -f Common/pom.xml install -DskipTests -B && \
    mvn -f TascaEats/pom.xml package -DskipTests -B

FROM eclipse-temurin:17
VOLUME /tmp
RUN mkdir -p /app/logs/
COPY --from=builder /workspace/TascaEats/target/tascaeats-1.0.jar /app/app.jar
EXPOSE 8080 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
