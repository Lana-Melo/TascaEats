FROM docker.io/maven:3.9-eclipse-temurin-17 as builder
COPY . /workspace
WORKDIR /workspace
RUN mvn clean package -Dmaven.test.skip=true

FROM docker.io/eclipse-temurin:17
VOLUME /tmp
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
COPY --from=builder /workspace/target/delivery-service-1.0.jar app/app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar", "/app/app.jar"]
