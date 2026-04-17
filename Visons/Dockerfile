# Stage 1: Build with Maven
FROM maven:3.9-amazoncorretto-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 9090

ENV DB_URL=jdbc:sqlserver://mssql-server:1433;databaseName=Visons;encrypt=true;trustServerCertificate=true
ENV DB_USER=sa
ENV DB_PASSWORD=Admin12345!
ENV PORT=9090
ENV SERVER_URL=http://localhost:9090

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=120s \
  CMD curl -f http://localhost:9090/swagger-ui.html || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]


# docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Admin12345" -p 1433:1433 --name sqlserver -d juan321/sql-server:2022

# docker build -t juan321/springboot-sqlserver:1.0 .

# docker run -d --name springboot-sqlserver -p 8085:8085 juan321/springboot-sqlserver:1.0

# docker push juan321/springboot-sqlserver:1.0
