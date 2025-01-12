FROM ghcr.io/graalvm/jdk-community:21 AS builder

RUN microdnf install maven -y

WORKDIR /app

COPY . .

RUN mvn clean compile spring-boot:process-aot package -DskipTests

FROM ghcr.io/graalvm/jdk-community:21 AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar", "-Dspring.aot.enabled=true"]