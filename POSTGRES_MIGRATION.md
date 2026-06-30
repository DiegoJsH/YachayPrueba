# Migración de MySQL a PostgreSQL

Este proyecto ha sido migrado de MySQL a PostgreSQL. Sigue estos pasos para ejecutar la aplicación localmente.

## Prerequisitos

- Docker y Docker Compose instalados
- Java 21 o superior
- Maven 3.8+

## Pasos para levantar el proyecto

### 1. Levantar PostgreSQL con Docker

```bash
docker-compose up -d
```

Este comando va a:
- Descargar la imagen de PostgreSQL 16 Alpine
- Crear un contenedor llamado `proyecto_herramientas_db`
- Crear la base de datos `herramientas_db`
- Configurar el usuario `herramientas_user` con contraseña `herramientas_password`
- Mapear el puerto 5432 (local) al 5432 del contenedor

### 2. Verificar que PostgreSQL está corriendo

```bash
docker-compose ps
```

Deberías ver el contenedor con estado `healthy`.

Alternatively, puedes conectar con psql:
```bash
docker exec -it proyecto_herramientas_db psql -U herramientas_user -d herramientas_db
```

### 3. Compilar y ejecutar la aplicación Spring Boot

```bash
mvn clean install
mvn spring-boot:run
```

O si usas el IDE, simplemente ejecuta la clase main de Spring Boot.

### 4. Verificar la conexión

La aplicación debería crear las tablas automáticamente (dependiendo de tu configuración de `spring.jpa.hibernate.ddl-auto`).

Revisa los logs de Spring Boot para confirmar que se conectó exitosamente a PostgreSQL.

## Cambios realizados

1. **pom.xml**: Reemplazado `mysql-connector-j` con `org.postgresql:postgresql`
2. **application.properties**: Actualizado el driver y dialect de Hibernate para PostgreSQL
3. **docker-compose.yml**: Creado para levantar PostgreSQL localmente
4. **.env.development.local**: Configuradas las variables de entorno para conectar a PostgreSQL

## Variables de entorno

```
DB_URL=jdbc:postgresql://localhost:5432/herramientas_db
DB_USER=herramientas_user
DB_PASSWORD=herramientas_password
```

Estas están configuradas en `.env.development.local` y serán cargadas automáticamente por Spring Boot.

## Importar datos desde MySQL (opcional)

Si tenías datos en MySQL, puedes exportarlos:

```bash
# Exportar desde MySQL
mysqldump -u usuario -p base_datos > backup.sql

# Adaptar el SQL para PostgreSQL (cambiar tipos de datos si es necesario)

# Importar a PostgreSQL
docker exec -i proyecto_herramientas_db psql -U herramientas_user -d herramientas_db < backup.sql
```

## Troubleshooting

### Error: "Could not connect to database"
- Verifica que Docker está corriendo: `docker ps`
- Verifica que el contenedor está en estado healthy: `docker-compose ps`
- Revisa los logs: `docker-compose logs postgres`

### Error: "Database does not exist"
- Asegúrate de ejecutar `docker-compose up -d` correctamente
- Verifica las credenciales en `.env.development.local`

### Limpiar todo y empezar de nuevo

```bash
# Detener contenedores
docker-compose down

# Eliminar volumen de datos
docker volume rm proyecto_herramientas_db_postgres_data

# Iniciar de nuevo
docker-compose up -d
```

## Detener PostgreSQL

```bash
docker-compose down
```

Esto detendrá el contenedor pero preservará los datos en el volumen.
