FROM ghcr.io/graalvm/jdk-community:21 AS builder

RUN microdnf install maven -y

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean compile spring-boot:process-aot package -DskipTests

FROM ghcr.io/graalvm/jdk-community:21 AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dspring.aot.enabled=true", "-jar", "app.jar"]