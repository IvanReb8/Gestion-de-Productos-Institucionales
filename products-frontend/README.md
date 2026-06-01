# Productos - Aplicación Backend y Frontend

Este repositorio contiene dos proyectos relacionados:

- `products/`: backend Spring Boot con API REST, seguridad JWT y acceso a base de datos MariaDB.
- `products-frontend/`: frontend Angular que consume la API de productos y maneja autenticación.

## Requisitos previos

- Java 17
- Maven (opcional, el proyecto incluye `mvnw` / `mvnw.cmd`)
- Node.js y npm
- MariaDB o MySQL compatible

## Ejecución del backend (Spring Boot)

1. Abrir una terminal en la carpeta `products/`.
2. Instalar dependencias y ejecutar la aplicación:

   - En Windows:
     ```powershell
     .\mvnw.cmd clean spring-boot:run
     ```

   - En Linux/macOS:
     ```bash
     ./mvnw clean spring-boot:run
     ```

3. La aplicación se ejecuta por defecto en:

   - `http://localhost:8080`

### Configuración de la base de datos

El backend usa MariaDB con la configuración actual en `products/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3307/catalog_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

Asegúrate de que la base de datos `catalog_db` exista y que el usuario tenga permisos. Si prefieres otra configuración, actualiza `application.properties` antes de iniciar la aplicación.

#### Inicializar la base de datos con `script.sql`

1. Abre tu cliente SQL o consola de MariaDB/MySQL.
2. Crea la base de datos si aún no existe:

   ```sql
   CREATE DATABASE IF NOT EXISTS catalog_db;
   ```

3. Carga el script SQL disponible en `products-frontend/script.sql`:

   ```sql
   USE catalog_db;
   SOURCE ../products-frontend/script.sql;
   ```

   > Si tu cliente no admite `SOURCE`, copia y ejecuta el contenido del archivo directamente.

4. Verifica que la tabla `products` y el índice `idx_prod_search` se hayan creado correctamente.

## Ejecución del frontend (Angular)

1. Abrir una terminal en la carpeta `products-frontend/`.
2. Instalar dependencias:

   ```bash
   npm install
   ```

3. Iniciar el servidor de desarrollo:

   ```bash
   npm start
   ```

4. Abrir el navegador en:

   - `http://localhost:4200`

## Scripts útiles

### Backend

- Compilar y empaquetar:
  ```bash
  ./mvnw clean package
  ```
- Ejecutar pruebas:
  ```bash
  ./mvnw test
  ```

### Frontend

- Compilar el proyecto:
  ```bash
  npm run build
  ```
- Ejecutar pruebas unitarias:
  ```bash
  npm test
  ```

## Notas adicionales

- El frontend espera que el backend esté disponible en `http://localhost:8080`.
- La autenticación usa JWT. Si hay problemas con el login, revisa la configuración de seguridad y el valor secreto en el backend.
- El backend está configurado para actualizar el esquema de la base de datos con `spring.jpa.hibernate.ddl-auto=update`.

## Estructura principal del repositorio

- `products/`: proyecto Spring Boot
  - `pom.xml`: dependencias y configuración Maven
  - `src/main/java`: código Java
  - `src/main/resources/application.properties`: configuración de la aplicación

- `products-frontend/`: proyecto Angular
  - `package.json`: dependencias y scripts npm
  - `src/`: código fuente de Angular

---