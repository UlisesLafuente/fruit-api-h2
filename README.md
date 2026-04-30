# Fruit API (Nivel 2)

REST API para gestión de frutas y proveedores con base de datos MySQL o H2 (tests).

## Características

- **CRUD completo** de frutas y proveedores
- **Relación @ManyToOne** entre Fruit y Provider
- **Validación** de datos con Jakarta Validation
- **Manejo de excepciones** centralizado (400, 404, 409)
- **Transaccionalidad** explícita en servicios
- **Tests** unitarios y de integración con H2
- **Docker** multi-stage build con docker-compose

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | - |
| MySQL | 8.0 |
| H2 (tests) | - |
| Maven | 3.9 |

## Endpoints

### Fruits

| Método | Endpoint | Descripción | Código |
|--------|----------|-------------|--------|
| GET | `/fruits` | Listar todas las frutas | 200 |
| GET | `/fruits?providerId={id}` | Listar frutas por proveedor | 200 / 404 |
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

### Con perfil por defecto (MySQL)
```bash
./mvnw spring-boot:run
```

### Tests (con H2)
```bash
./mvnw test
```

### Con Docker Compose
```bash
docker-compose up -d
./mvnw spring-boot:run
```

## Configuración

La aplicación es configurable mediante environment variables o application.properties:

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/fruit_db` | URL de MySQL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Usuario de BD |
| `SPRING_DATASOURCE_PASSWORD` | `root` | Password de BD |

## Tests

```bash
./mvnw test
```

**Total: 51 tests**
- FruitServiceTest: 11 tests
- ProviderServiceTest: 13 tests
- FruitControllerIntegrationTest: 13 tests
- ProviderControllerIntegrationTest: 13 tests
- FruitApiH2ApplicationTests: 1 test

## Estructura del proyecto

```
src/main/java/cat/itacademy/s04/t02/n02/
├── FruitApiH2Application.java
├── common/exception/
│   ├── DomainException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── NotFoundException.java
│   └── ResourceNotFoundException.java
├── fruit/
│   ├── controllers/
│   │   └── FruitController.java
│   ├── dto/
│   │   ├── FruitRequestDto.java
│   │   └── FruitResponseDto.java
│   ├── model/
│   │   └── Fruit.java
│   ├── repository/
│   │   └── FruitRepository.java
│   └── services/
│       └── FruitService.java
└── provider/
    ├── controllers/
    │   └── ProviderController.java
    ├── dto/
    │   ├── ProviderRequestDto.java
    │   └── ProviderResponseDto.java
    ├── exception/
    │   ├── ProviderAlreadyExistsException.java
    │   ├── ProviderHasFruitsException.java
    │   └── ProviderNotFoundException.java
    ├── model/
    │   └── Provider.java
    ├── repository/
    │   └── ProviderRepository.java
    └── service/
        └── ProviderService.java
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
                      │  (record)   │     │            │
                      └─────────────┘     └─────────────┘
                                            │
                                      ┌─────┴─────┐
                                      │  MySQL    │
                                      └───────────┘
```

## Errores esperados

| Código | Descripción |
|--------|-------------|
| 400 | Validación de datos incorrectos o regla de negocio |
| 404 | Recurso no encontrado |
| 409 | Conflicto (proveedor duplicado) |

## Build

```bash
./mvnw clean package -DskipTests
```

Genera: `target/fruit-api-MySQL-0.0.1-SNAPSHOT.jar`

## Historial de cambios

### 30 de abril de 2026
- DTOs refactorizados a records Java
- Separación de DTOs de entrada y salida
--weightInKilos cambiado a Integer con validación correcta
- Añadida validación de proveedor en filtro de frutas
- Añadida validación de nombre duplicado en actualización
- Añadida restricción única en Provider (BD)
- Jerarquía de excepciones simplificada
- Transaccionalidad explícita en servicios
- Tests configurados con H2

Ver [doc/corrections.md](./doc/corrections.md) para detalles completos.

## Licencia

Este proyecto está licenciado bajo la **Apache License 2.0**. Ver el archivo [LICENSE.txt](./LICENSE.txt) para más detalles.

```
Copyright 2026 Ulises Lafuente

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
```