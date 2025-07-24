# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn/wrapper/ .mvn/wrapper/
COPY data /app/data

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/smartchat-0.0.1-SNAPSHOT.jar app.jar
COPY --from=builder /app/data/h2db.mv.db /app/data/h2db.mv.db

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

RUN addgroup --system spring && adduser --system --ingroup spring spring
RUN chown -R spring:spring /app/data
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/heap-dump.hprof", "-jar", "/app/app.jar"]

