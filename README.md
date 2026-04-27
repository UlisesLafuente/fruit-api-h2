# Fruit API MySQL (Nivel 2)

REST API para gestión de frutas y proveedores con base de datos MySQL.

## Características

- **CRUD completo** de frutas y proveedores
- **Relación @ManyToOne** entre Fruit y Provider
- **Validación** de datos con Jakarta Validation
- **Manejo de excepciones** centralizado (400, 404, 409)
- **Base de datos MySQL** con persistencia
- **Tests** unitarios y de integración
- **Docker** multi-stage build con docker-compose

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | - |
| MySQL | 8.0 |
| Maven | 3.9 |

## Endpoints

### Fruits

| Método | Endpoint | Descripción | Código |
|--------|----------|-------------|--------|
| GET | `/fruits` | Listar todas las frutas | 200 |
| GET | `/fruits?providerId={id}` | Listar frutas por proveedor | 200 |
| GET | `/fruits/{id}` | Obtener fruta por ID | 200 / 404 |
| POST | `/fruits` | Crear fruta | 201 / 400 / 404 |
| PUT | `/fruits/{id}` | Actualizar fruta | 200 / 404 |
| DELETE | `/fruits/{id}` | Eliminar fruta | 204 / 404 |

### Providers

| Método | Endpoint | Descripción | Código |
|--------|----------|-------------|--------|
| GET | `/providers` | Listar todos los proveedores | 200 |
| GET | `/providers/{id}` | Obtener proveedor por ID | 200 / 404 |
| GET | `/providers/{id}/fruits` | Listar frutas de un proveedor | 200 / 404 |
| POST | `/providers` | Crear proveedor | 201 / 400 / 409 |
| PUT | `/providers/{id}` | Actualizar proveedor | 200 / 404 / 409 |
| DELETE | `/providers/{id}` | Eliminar proveedor | 204 / 404 / 400 |

### Ejemplos de uso

**Crear proveedor:**
```bash
curl -X POST http://localhost:8080/providers \
  -H "Content-Type: application/json" \
  -d '{"name": "Supplier1", "country": "Spain"}'
```

**Crear fruta con proveedor:**
```bash
curl -X POST http://localhost:8080/fruits \
  -H "Content-Type: application/json" \
  -d '{"name": "Apple", "weightInKilos": 2, "providerId": 1}'
```

**Listar frutas por proveedor:**
```bash
curl http://localhost:8080/fruits?providerId=1
```

**Obtener frutas de un proveedor específico:**
```bash
curl http://localhost:8080/providers/1/fruits
```

**Listar proveedores:**
```bash
curl http://localhost:8080/providers
```

**Actualizar proveedor:**
```bash
curl -X PUT http://localhost:8080/providers/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Supplier1Updated", "country": "France"}'
```

**Eliminar proveedor (sin frutas asociadas):**
```bash
curl -X DELETE http://localhost:8080/providers/1
```

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 (o Docker)

### Opción 1: Con MySQL externo
```bash
./mvnw spring-boot:run
```

### Opción 2: Con Docker Compose
```bash
docker-compose up -d
./mvnw spring-boot:run
```

### Opción 3: Con Docker
```bash
docker build -t fruit-api-mysql .
docker run -p 8080:8080 --network host fruit-api-mysql
```

## Configuración

La aplicación es configurable mediante environment variables:

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/fruit_db` | URL de MySQL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Usuario de BD |
| `SPRING_DATASOURCE_PASSWORD` | `root` | Password de BD |
| `SPRING_JPA_DATABASE_PLATFORM` | `org.hibernate.dialect.MySQLDialect` | Dialecto Hibernate |

## Tests

```bash
./mvnw test
```

**Cobertura:**
- 11 tests unitarios (FruitService)
- 11 tests unitarios (ProviderService)
- 16 tests de integración (FruitController)
- 12 tests de integración (ProviderController)

## Estructura del proyecto

```
src/main/java/cat/itacademy/s04/t02/n02/
├── fruit/
│   ├── controllers/
│   │   └── FruitController.java
│   ├── dto/
│   │   └── FruitDto.java
│   ├── exception/
│   │   ├── ErrorResponse.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── model/
│   │   └── Fruit.java
│   ├── repository/
│   │   └── FruitRepository.java
│   └── services/
│       └── FruitService.java
├── provider/
│   ├── controllers/
│   │   └── ProviderController.java
│   ├── dto/
│   │   └── ProviderDto.java
│   ├── exception/
│   │   ├── ProviderAlreadyExistsException.java
│   │   ├── ProviderHasFruitsException.java
│   │   └── ProviderNotFoundException.java
│   ├── model/
│   │   └── Provider.java
│   ├── repository/
│   │   └── ProviderRepository.java
│   └── service/
│       └── ProviderService.java
└── FruitApiH2Application.java
```

## Diagrama de arquitectura

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ Controller  │────▶│  Service    │────▶│ Repository  │
└─────────────┘     └─────────────┘     └─────────────┘
                           │                    │
                           ▼                    ▼
                      ┌─────────────┐     ┌─────────────┐
                      │    DTO      │     │   Entity    │
                      └─────────────┘     └─────────────┘
                                                 │
                                           ┌─────┴─────┐
                                           │  MySQL    │
                                           └───────────┘
```

## Errores esperados

| Código | Descripción |
|--------|-------------|
| 400 | Validación de datos incorrectos |
| 404 | Recurso no encontrado |
| 409 | Conflicto (proveedor duplicado) |

## Build

```bash
./mvnw clean package -DskipTests
```

Genera: `target/fruit-api-MySQL-0.0.1-SNAPSHOT.jar`

## Licencia

Este proyecto está licenciado bajo la **Apache License 2.0**. Ver el archivo [LICENSE.txt](./LICENSE.txt) para más detalles.

```
Copyright 2026 Ulises Lafuente

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
```