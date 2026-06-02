FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

COPY Common/ Common/
COPY DeliveryService/ DeliveryService/

RUN mvn -f Common/pom.xml install -DskipTests -B && \
    mvn -f DeliveryService/pom.xml package -DskipTests -B

FROM eclipse-temurin:17
VOLUME /tmp
RUN mkdir -p /app/logs/
COPY --from=builder /workspace/DeliveryService/target/delivery-service-1.0.jar /app/app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
