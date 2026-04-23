# Fruit API H2

REST API para gestión de frutas con base de datos H2 embebida.

## Características

- **CRUD completo** de frutas (Create, Read, Update, Delete)
- **Validación** de datos con Jakarta Validation
- **Manejo de excepciones** centralizado (400, 404)
- **Base de datos H2** file-based (persistente)
- **Tests** unitarios y de integración
- **Docker** multi-stage build

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | - |
| H2 Database | - |
| Maven | 3.9 |

## Endpoints

| Método | Endpoint | Descripción | Código |
|--------|----------|-------------|--------|
| GET | `/fruits` | Listar todas las frutas | 200 |
| GET | `/fruits/{id}` | Obtener fruta por ID | 200 / 404 |
| POST | `/fruits` | Crear fruta | 201 |
| PUT | `/fruits/{id}` | Actualizar fruta | 200 / 404 |
| DELETE | `/fruits/{id}` | Eliminar fruta | 204 / 404 |

### Ejemplos de uso

**Crear fruta:**
```bash
curl -X POST http://localhost:8080/fruits \
  -H "Content-Type: application/json" \
  -d '{"name": "Apple", "weightInKilos": 2}'
```

**Listar frutas:**
```bash
curl http://localhost:8080/fruits
```

**Obtener fruta por ID:**
```bash
curl http://localhost:8080/fruits/1
```

**Actualizar fruta:**
```bash
curl -X PUT http://localhost:8080/fruits/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Banana", "weightInKilos": 3}'
```

**Eliminar fruta:**
```bash
curl -X DELETE http://localhost:8080/fruits/1
```

## Ejecución local

### Con Maven
```bash
./mvnw spring-boot:run
```

### Con JAR
```bash
./mvnw package -DskipTests
java -jar target/fruit-api-h2-0.0.1-SNAPSHOT.jar
```

### Con Docker
```bash
docker build -t fruit-api .
docker run -p 8080:8080 fruit-api
```

## Configuración

La aplicación configurable mediante environment variables:

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:h2:file:./data/fruitdb` | URL de H2 |
| `SPRING_DATASOURCE_USERNAME` | `sa` | Usuario de BD |
| `SPRING_DATASOURCE_PASSWORD` | (vacío) | Password de BD |

## Consola H2

Disponible en: http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:file:./data/fruitdb`
- Username: `sa`
- Password: (vacío)

## Tests

```bash
./mvnw test
```

**Cobertura:**
- 8 tests unitarios (Service)
- 9 tests de integración (Controller)
- 1 test de contexto

## Estructura del proyecto

```
src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/
├── controllers/
│   └── FruitController.java
├── dto/
│   └── FruitDto.java
├── exception/
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   └── Fruit.java
├── repository/
│   └── FruitRepository.java
├── services/
│   └── FruitService.java
└── FruitApiH2Application.java
```

## Build

```bash
./mvnw clean package -DskipTests
```

Genera: `target/fruit-api-h2-0.0.1-SNAPSHOT.jar` (~54MB)