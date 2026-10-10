# ElectroNorte — Backend

Backend del sistema de Mesa de Ayuda para la Gestión de Incidencias del Servicio de Energía Eléctrica.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security y JWT
- Spring Data JPA / Hibernate
- Maven
- MySQL

## Requisitos

- JDK 17
- MySQL compatible
- Maven o Maven Wrapper
- IntelliJ IDEA u otro IDE compatible

## Configuración

1. Clonar el repositorio.
2. Crear una base de datos MySQL llamada `mesa_ayuda_db`.
3. Configurar las variables de entorno utilizadas por `application.properties`, incluyendo las credenciales de MySQL y `JWT_SECRET`.
4. Verificar que la configuración de conexión apunte a la base de datos correspondiente.

## Ejecución local

En Windows, abrir una terminal en la raíz del proyecto y ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

La dirección local y el puerto dependen de la configuración del proyecto.

## API REST

El backend proporciona servicios para autenticación, gestión de usuarios e incidencias.

Ejemplos de endpoints:

- `POST /api/auth/login`
- `POST /api/incidencias`
- `GET /api/prueba`

Los endpoints protegidos requieren autenticación JWT.

## Frontend

Repositorio del frontend:

https://github.com/AlexanderDF21/ElectroNorte-frontend