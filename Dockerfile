# Stage 1: Build
FROM amazoncorretto:17-alpine-jdk as builder
WORKDIR /build

# Copiar pom.xml
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias en caché
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copiar código fuente
COPY src src

# Compilar
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# Copiar JAR del build
COPY --from=builder /build/target/yachaytinkiy-0.0.1-SNAPSHOT.jar /app/yachaytinkiy.jar

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV PORT=8080

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/catalogo || exit 1

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "/app/yachaytinkiy.jar"]
