# Visons Backend - Multi-stage Build
# Stage 1: Compile with Maven
FROM maven:3.9.11-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src/ src/
RUN mvn clean package -DskipTests

# Stage 2: Runtime with JRE
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/target/Visons-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

ENV DB_URL=jdbc:sqlserver://mssql-server:1433;databaseName=Visons;encrypt=true;trustServerCertificate=true
ENV DB_USER=sa
ENV DB_PASSWORD=Admin12345!
ENV PORT=9090
ENV SERVER_URL=http://localhost:9090

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=120s \
  CMD curl -f http://localhost:9090/swagger-ui.html || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
# ============================================================================
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=5 \
    CMD curl -f http://localhost:9090/swagger-ui.html || exit 1

# ============================================================================
# Comando de inicio
# Ejecuta el JAR con variables de entorno
# ============================================================================
ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]
