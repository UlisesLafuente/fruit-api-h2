# Tutorial: Fruit API H2 - Explicación Clase por Clase

Este documento es un tutorial completo que explica cómo está estructurada la aplicación Fruit API H2, qué función cumple cada clase, y cómo trabajan juntas para crear una API REST funcional.

---

## 1. Visión General de la Arquitectura

Antes de entrar en detalles, es importante entender la estructura general de la aplicación. Esta sigue el patrón **MVC (Model-View-Controller)** adaptado para APIs REST, junto con otros patrones de diseño:

```
┌─────────────────────────────────────────────────────────────────┐
│                        CONTROLADOR                               │
│                    (FruitController)                             │
│                                                                  │
│  Recibe HTTP Requests → Valida → Llama al Servicio              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICIO                                   │
│                      (FruitService)                              │
│                                                                  │
│  Lógica de negocio → Transforma datos → Valida reglas           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        REPOSITORIO                               │
│                    (FruitRepository)                             │
│                                                                  │
│  Acceso a datos → consultas SQL → Persistencia H2               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         MODELO                                   │
│                        (Fruit)                                   │
│                                                                  │
│  Representación de la entidad en la base de datos               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. FruitApiH2Application.java

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/FruitApiH2Application.java`

### ¿Qué es?

Es el **punto de entrada** de la aplicación Spring Boot. Es la clase principal que contiene el método `main()`.

### ¿Por qué existe?

Toda aplicación Java necesita un método `main()` para ejecutarse. Esta clase usa la anotación `@SpringBootApplication` que组合三个注解:

1. `@SpringBootConfiguration` - Permite configurar la aplicación mediante código Java
2. `@EnableAutoConfiguration` - Spring Boot configura automáticamente componentes basados en dependencias
3. `@ComponentScan` - Escanea el paquete actual y subpaquetes para encontrar componentes

### Código:

```java
@SpringBootApplication
public class FruitApiH2Application {
    public static void main(String[] args) {
        SpringApplication.run(FruitApiH2Application.class, args);
    }
}
```

### Flujo de ejecución:

```
main() → SpringApplication.run() → Escanea componentes → Inicia servidor embebido (Tomcat) en puerto 8080
```

---

## 3. Fruit.java (Modelo/Entidad)

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/model/Fruit.java`

### ¿Qué es?

Representa la **tabla de la base de datos** como un objeto Java. En términos de JPA (Java Persistence API), es una **Entidad**.

### ¿Por qué existe?

Necesitamos una representación de nuestros datos en Java. Esta clase mapea directamente a una tabla en H2 llamada `FRUIT` (el nombre de la tabla se infiere del nombre de la clase).

### Anotaciones utilizadas:

| Anotación | Propósito |
|-----------|-----------|
| `@Entity` | Indica que esta clase es una entidad JPA y debe persistirse en la BD |
| `@Id` | Marca el campo como la clave primaria |
| `@GeneratedValue` | Generación automática del ID (auto-increment) |
| `@Getter` (Lombok) | Genera automáticamente getter para todos los campos |
| `@Setter` (Lombok) | Genera automáticamente setter para todos los campos |

### Código:

```java
@Getter
@Setter
@Entity
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int weightInKilos;
}
```

### Tabla en H2:

| Columna | Tipo |
|---------|------|
| ID | BIGINT (auto-generated) |
| NAME | VARCHAR |
| WEIGHT_IN_KILOS | INT |

---

## 4. FruitDto.java (Data Transfer Object)

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/dto/FruitDto.java`

### ¿Qué es?

Un **DTO (Data Transfer Object)** - un objeto simple para transferir datos entre capas.

### ¿Por qué existe? - Importante

Esta es una decisión de diseño crucial. ¿Por qué no usar directamente la entidad `Fruit` en el Controller?

**Razones para usar DTO:**

1. **Abstracción**: El cliente de la API no necesita conocer la estructura interna de la base de datos
2. **Validación**: Podemos aplicar validaciones específicas para entrada/salida
3. **Seguridad**: Podemos excluir campos sensibles (ej: password) o agregar otros
4. **Control de versiones**: Si cambia la estructura de BD, la API puede mantenerse compatible
5. **Separación de responsabilidades**: La entidad JPA solo关乎 la persistencia; el DTO关乎 la comunicación

### Anotaciones de validación:

| Anotación | Propósito |
|-----------|-----------|
| `@NotBlank` | El campo no puede ser null ni estar vacío (solo para Strings) |
| `@NotNull` | El campo no puede ser null |
| `@Positive` | El número debe ser mayor que 0 |

### Código:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FruitDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private int weightInKilos;
    
    // Métodos de mapeo
    public static FruitDto fromEntity(Fruit fruit) { ... }
    public Fruit toEntity() { ... }
}
```

### Patrón utilizado: **Mapper**

Los métodos `fromEntity()` y `toEntity()` implementan el patrón **Mapper** para convertir entre Entity y DTO. Esto mantiene el código limpio y evita duplicación.

---

## 5. FruitRepository.java (Repositorio)

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/repository/FruitRepository.java`

### ¿Qué es?

Una **interfaz** que extiende `JpaRepository` de Spring Data JPA.

### ¿Por qué existe?

Proporciona todos los métodos CRUD sin escribir código SQL:

| Método | Función |
|--------|---------|
| `save(entity)` | Insertar o actualizar |
| `findById(id)` | Buscar por ID |
| `findAll()` | Listar todos |
| `deleteById(id)` | Eliminar por ID |
| `existsById(id)` | Verificar si existe |

### Código:

```java
@Repository
public interface FruitRepository extends JpaRepository<Fruit, Long> {
}
```

### ¿Por qué es una interfaz?

Spring Data JPA usa **proxies**: en tiempo de ejecución, Spring crea una implementación automáticamente. Solo definimos la "firma" (qué métodos queremos) y Spring genera el código que interacts con la base de datos.

### Patrón utilizado: **Repository**

Es el clásico **Repository Pattern** que abstrae el acceso a datos. El Service no sabe cómo se persisten los datos (podría ser H2, MySQL, archivo, etc.).

---

## 6. FruitService.java (Servicio)

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/services/FruitService.java`

### ¿Qué es?

La **capa de lógica de negocio** (también llamada capa de dominio o aplicación).

### ¿Por qué existe?

Separa la lógica de negocio delController. El Controller solo debe manejar HTTP; el Service contiene las reglas del negocio.

### Código:

```java
@Service
public class FruitService {
    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public FruitDto addFruit(FruitDto fruitDto) { ... }
    public List<FruitDto> getAllFruits() { ... }
    public FruitDto getFruitById(Long id) { ... }
    public FruitDto updateFruit(Long id, FruitDto fruitDto) { ... }
    public void deleteFruit(Long id) { ... }
}
```

### Métodos explicados:

1. **addFruit**: Convierte DTO a Entity, guarda en BD, devuelve DTO
2. **getAllFruits**: Obtiene todos los registros, convierte cada uno a DTO
3. **getFruitById**: Busca por ID, lanza excepción si no existe
4. **updateFruit**: Verifica existencia, actualiza, lanza excepción si no existe
5. **deleteFruit**: Verifica existencia, elimina, lanza excepción si no existe

### Patrones utilizados:

- **Dependency Injection**: El Repository se inyecta en el Constructor
- **Single Responsibility**: Cada método tiene una responsabilidad única
- **Exception Handling**: Lanza `ResourceNotFoundException` cuando no encuentra recursos

---

## 7. FruitController.java (Controlador)

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/controllers/FruitController.java`

### ¿Qué es?

El **Controller** maneja las peticiones HTTP y devuelve respuestas.

### ¿Por qué existe?

Es el punto de entrada de la API REST. Traduce HTTP requests a llamadas al Service y HTTP responses a los clientes.

### Anotaciones:

| Anotación | Propósito |
|-----------|-----------|
| `@RestController` | Combina `@Controller` + `@ResponseBody` (JSON automático) |
| `@RequestMapping("/fruits")` | Prefijo para todas las rutas |

### Métodos HTTP:

| Método | Anotación | Descripción |
|--------|-----------|-------------|
| POST | `@PostMapping` | Crear recurso |
| GET | `@GetMapping` | Obtener recurso(s) |
| PUT | `@PutMapping` | Actualizar recurso |
| DELETE | `@DeleteMapping` | Eliminar recurso |

### Código:

```java
@RestController
@RequestMapping("/fruits")
public class FruitController {
    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitDto> addFruit(@Valid @RequestBody FruitDto fruitDto) { ... }

    @GetMapping
    public ResponseEntity<List<FruitDto>> getAllFruits() { ... }

    @GetMapping("/{id}")
    public ResponseEntity<FruitDto> getFruitById(@PathVariable Long id) { ... }

    @PutMapping("/{id}")
    public ResponseEntity<FruitDto> updateFruit(@PathVariable Long id, 
                                                  @Valid @RequestBody FruitDto fruitDto) { ... }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Long id) { ... }
}
```

### Anotaciones importantes:

- `@Valid`: Activa la validación del DTO (las anotaciones como `@NotBlank`)
- `@RequestBody`: Convierte el JSON entrante a objeto Java
- `@PathVariable`: Extrae el `{id}` de la URL

### Códigos HTTP devueltos:

| Método | Código | Significado |
|--------|--------|-------------|
| POST | 201 Created | Recurso creado exitosamente |
| GET (exito) | 200 OK | Respuesta exitosa |
| GET (no encontrado) | 404 Not Found | Recurso no existe |
| PUT (exito) | 200 OK | Actualización exitosa |
| PUT (no encontrado) | 404 Not Found | Recurso no existe |
| DELETE (exito) | 204 No Content | Eliminación exitosa (sin cuerpo) |
| DELETE (no encontrado) | 404 Not Found | Recurso no existe |

---

## 8. Excepciones

### ResourceNotFoundException.java

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/exception/ResourceNotFoundException.java`

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**¿Por qué existe?**
Para indicar que un recurso solicitado no fue encontrado. Es una excepción personalizada que se lanza cuando `findById()` retorna vacío.

**Patrón utilizado: Custom Exception**
Extender `RuntimeException` la convierte en una excepción no verificada (unchecked), lo que significa que no requiere `throws` en la firma del método.

---

### ErrorResponse.java

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/exception/ErrorResponse.java`

```java
public record ErrorResponse(int status, String message, List<String> errors) {
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }
}
```

**¿Por qué existe?**
Para estandarizar las respuestas de error en formato JSON.

**¿Por qué un `record`?**
Es una característica de Java 16+ que crea automáticamente:
- Constructor con parámetros
- Getters (`status()`, no `getStatus()`)
- `equals()` y `hashCode()`
- `toString()`

Es más conciso que una clase con getters/setters.

---

### GlobalExceptionHandler.java

**Ubicación:** `src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/exception/GlobalExceptionHandler.java`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Validation failed", errors));
    }
}
```

**¿Por qué existe?**
Maneja **todas las excepciones** de forma centralizada. En lugar de usar try-catch en cada método del Controller, las excepciones fluyen hasta aquí.

**Anotaciones:**

- `@RestControllerAdvice`: Combina `@ControllerAdvice` + `@ResponseBody`. Intercepta todas las excepciones de todos los Controllers REST
- `@ExceptionHandler`: Define qué tipo de excepción maneja cada método

**Patrón: Exception Handling Centralizado**
En lugar de manejar errores en cada endpoint, hay un único lugar que decide qué hacer cuando ocurre una excepción.

---

## 9. Flujo Completo de una Petición

Veamos el flujo completo cuando un cliente hace una petición:

### Ejemplo: `GET /fruits/1`

```
1. CLIENTE
   │
   │ HTTP GET /fruits/1
   ▼
2. FRUIT CONTROLLER
   │ @GetMapping("/{id}")
   │ extracte id=1 del path
   ▼
3. FRUIT SERVICE
   │ fruitRepository.findById(1)
   │ Si no existe → lanza ResourceNotFoundException
   ▼
4. FRUIT REPOSITORY
   │ SELECT * FROM FRUIT WHERE ID = 1
   ▼
5. H2 DATABASE
   │
   │ Retorna fila encontrada
   ▼
6. FRUIT (Entity) → FRUIT DTO
   │ fruitDto = FruitDto.fromEntity(fruit)
   ▼
7. FRUIT CONTROLLER
   │ return ResponseEntity.ok(fruitDto)
   ▼
8. CLIENTE
   │ HTTP 200 OK + JSON: {"id":1,"name":"Apple","weightInKilos":2}
```

---

## 10. Resumen de Patrones de Diseño

| Patrón | Dónde se aplica |
|--------|----------------|
| **MVC** | Controller → Service → Repository |
| **DTO** | FruitDto para transferencia de datos |
| **Repository** | FruitRepository para acceso a datos |
| **Dependency Injection** | Constructor injection en Service y Controller |
| **Mapper** | fromEntity() / toEntity() en DTO |
| **Exception Handling** | GlobalExceptionHandler centralizado |
| **Custom Exception** | ResourceNotFoundException |

---

## 11. Archivos de Configuración

### application.properties

**Ubicación:** `src/main/resources/application.properties`

```properties
spring.application.name=fruit-api-h2

# Configuración de H2 con soporte para environment variables
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:file:./data/fruitdb}
spring.datasource.driverClassName=${SPRING_DATASOURCE_DRIVER_CLASS_NAME:org.h2.Driver}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}

spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM:org.hibernate.dialect.H2Dialect}
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:true}

spring.h2.console.enabled=${SPRING_H2_CONSOLE_ENABLED:true}
spring.h2.console.path=${SPRING_H2_CONSOLE_PATH:/h2-console}
```

**Significado de las propiedades:**

- `ddl-auto=update`: Hibernate crea/actualiza tablas automáticamente
- `show-sql=true`: Muestra SQL en consola (útil para debug)
- La sintaxis `${VAR:default}` significa: usar variable de entorno o valor por defecto

---

## 12. Tests

### FruitServiceTest.java

Tests unitarios usando **Mockito** para el Service:

- **@Mock**: Crea un mock del Repository
- **@InjectMocks**: Inyecta el mock en el Service
- **@ExtendWith(MockitoExtension.class)**: Habilita Mockito para JUnit 5

### FruitControllerIntegrationTest.java

Tests de integración con `@SpringBootTest`:

- Levanta toda la aplicación
- Usa `MockMvc` para simular HTTP requests
- Verifica códigos de respuesta y contenido JSON

---

## 13. Docker

### Dockerfile (Multi-stage Build)

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir -p /app/data
COPY --from=build /app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/fruitdb

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**¿Por qué multi-stage?**
El primer stage usa Maven para compilar (imagen grande). El segundo stage copia solo el JAR a una imagen Alpine ligera (~150MB vs 1GB+).

---

## 14. Conclusión

Esta aplicación demuestra las **mejores prácticas** para crear APIs REST con Spring Boot:

1. **Separación de responsabilidades**: Controller → Service → Repository
2. **DTOs** para abstraer la estructura de datos
3. **Validación** centralizada con annotations
4. **Manejo de errores** global
5. **Tests** unitarios y de integración
6. **Configuración externalizada** mediante environment variables
7. **Docker** para despliegue reproducible

Cada clase tiene un propósito claro y específico, facilitando el mantenimiento y la evolución del código.