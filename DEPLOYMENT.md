# Guía de Deployment - YachayTinkiy

## Variables de Entorno Requeridas

Tu aplicación Spring Boot requiere las siguientes variables de entorno. Debes configurarlas en tu plataforma de deployment (Render, Railway, Vercel, etc.).

### Base de Datos PostgreSQL

```
DB_URL=jdbc:postgresql://host:puerto/nombre_base_datos
DB_USERNAME=usuario_postgres
DB_PASSWORD=contraseña_postgres
```

**Ejemplo:**
```
DB_URL=jdbc:postgresql://dpg-d91iv5mq1p3s73c28a00-a/yachaytinkiy
DB_USERNAME=yachaytinkiy_user
DB_PASSWORD=tu_contraseña_segura
```

### PDFMonkey (Generación de PDFs)

```
PDFMONKEY_API_KEY=tu_clave_api
PDFMONKEY_TEMPLATE_ID=tu_template_id
```

## Pasos para Deploy en Render

1. **Conecta tu repositorio GitHub** a Render
2. **Ve a Environment** en la configuración del servicio
3. **Agrega las variables de entorno:**
   - DB_URL
   - DB_USERNAME
   - DB_PASSWORD
   - PDFMONKEY_API_KEY (opcional si no usas PDFMonkey)
   - PDFMONKEY_TEMPLATE_ID (opcional si no usas PDFMonkey)

4. **Deploy automático** se activará cuando hagas push a `main`

## Pasos para Deploy en Railway

1. **Conecta tu repositorio GitHub**
2. **Ve a Variables** en Railway Dashboard
3. **Agrega cada variable de entorno**
4. **Deploy automático** se activará

## Pasos para Deploy Local (Desarrollo)

1. Crea un archivo `.env` en la raíz del proyecto (NO lo commits):
   ```
   DB_URL=jdbc:postgresql://localhost:5432/yachaytinkiy
   DB_USERNAME=yachaytinkiy_user
   DB_PASSWORD=contraseña_local
   ```

2. Ejecuta:
   ```bash
   export $(cat .env | xargs)
   ./mvnw spring-boot:run
   ```

## Seguridad

⚠️ **IMPORTANTE:**
- NUNCA hardcodees credenciales en `application.properties`
- NUNCA commits el archivo `.env` (agrega a `.gitignore`)
- Usa variables de entorno SIEMPRE en producción
- Cambia las contraseñas en producción

## Verificación

Después de deploy, verifica que tu aplicación esté corriendo correctamente:
- La base de datos debe conectarse sin errores
- Deberías ver logs de Hibernate creando/actualizando tablas
- El servidor debe estar disponible en el puerto configurado

