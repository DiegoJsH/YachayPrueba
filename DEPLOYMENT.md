# Guía de Deployment - YachayTinkiy

## ✅ Configuración Actual

Tu aplicación está **hardcodeada** con los siguientes datos:

### Base de Datos PostgreSQL (Render)
```
URL: jdbc:postgresql://dpg-d91iv5mq1p3s73c28a00-a.oregon-postgres.render.com/yachaytinkiy
Usuario: yachaytinkiy_user
Contraseña: ✓ Configurada en application.properties
```

### PDFMonkey (API de Certificados)
```
API Key: ✓ Configurada en application.properties
Template ID: ✓ Configurada en application.properties
```

## 🚀 Deploy en Render (Paso a Paso)

### 1. Prerequisitos
- ✅ Repositorio GitHub conectado
- ✅ Código pusheado a `main`
- ✅ Credenciales BD configuradas en `application.properties`

### 2. Crear Servicio en Render

1. Ve a [https://render.com/dashboard](https://render.com/dashboard)
2. Click en **"New +"** → **"Web Service"**
3. Selecciona tu repositorio GitHub: **DiegoJsH/YachayPrueba**
4. Configura:
   - **Name:** `yachay-tinkiy`
   - **Environment:** Docker
   - **Region:** Elige la más cercana
   - **Branch:** `main`

### 3. Build & Deploy Settings

- **Build Command:** Leave default (usa `Dockerfile`)
- **Start Command:** Leave default (define en `Dockerfile`)

### 4. Plans
- Elige plan según tu necesidad (Free plan = $0 pero se duerme)

### 5. Deploy
- Click en **"Deploy"**
- Espera 5-10 minutos mientras se construye y deploy

## 📋 Checklist Pre-Deploy

Asegúrate de que todo esté correcto:

- [ ] `Dockerfile` está actualizado (Java 17 ✓)
- [ ] `application.properties` tiene credenciales ✓
- [ ] `pom.xml` tiene versión Java 17 ✓
- [ ] `.dockerignore` existe ✓
- [ ] Código está en `main` branch ✓
- [ ] No hay errores en tests locales

## 🔍 Verificación Post-Deploy

1. Accede a tu URL (ej: `https://yachay-tinkiy.onrender.com`)
2. Debería mostrar la página de login
3. Revisa los logs en Render Dashboard:
   ```
   "Hibernate creating tables..."
   "Server started at port 8080"
   "Application started successfully"
   ```

## ⚠️ Posibles Problemas

### Error: "Connection refused"
- Verifica que los datos de BD en `application.properties` sean correctos
- Renderization de la BD puede tomar varios minutos

### Error: "Out of Memory"
- Aumenta el plan en Render
- Verifica `JAVA_OPTS` en Dockerfile

### La aplicación se duerme (Free Plan)
- Elige plan pagado o usa Railway/Vercel
- Upgrade a "Starter" plan en Render ($7/mes)

## 📝 Pasos Rápidos para hacer Build Local

```bash
# Compilar JAR
./mvnw clean package

# Ver target/
ls target/yachaytinkiy-0.0.1-SNAPSHOT.jar

# Build Docker image (opcional para probar localmente)
docker build -t yachay-tinkiy .
docker run -p 8080:8080 yachay-tinkiy
```

## 🔐 Notas de Seguridad

⚠️ **Tus credenciales están hardcodeadas en el repositorio:**
- Las credenciales de BD están visibles en `application.properties`
- Las credenciales de PDFMonkey también están expuestas
- Si este es un proyecto real/producción, considera usar variables de entorno

**Para producción real:**
```bash
# En lugar de hardcodear, usa variables de entorno en Render
export DB_URL=...
export DB_USERNAME=...
export DB_PASSWORD=...
```

