# Anotaciones en la Aplicación

Este documento lista todas las anotaciones (annotations) utilizadas en el proyecto, organizadas por categoría.

---

## 1. Anotaciones de Spring Boot

### @SpringBootApplication
**Ubicación:** Clase principal (`FruitApiH2Application.java`)

Combina tres anotaciones:
- `@SpringBootConfiguration` - Permite configuración Java
- `@EnableAutoConfiguration` - Configuración automática de componentes
- `@ComponentScan` - Escaneo de componentes en el paquete actual

```java
@SpringBootApplication
public class FruitApiH2Application { ... }
```

---

## 2. Anotaciones de Spring MVC (REST)

### @RestController
**Ubicación:** `FruitController.java`

Indica que la clase es un Controller REST. Combina `@Controller` + `@ResponseBody` (convierte automáticamente a JSON).

```java
@RestController
public class FruitController { ... }
```

### @RequestMapping
**Ubicación:** `FruitController.java`

Define el prefijo de URL para todos los endpoints del controller.

```java
@RequestMapping("/fruits")
public class FruitController { ... }
```

### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
**Ubicación:** `FruitController.java`

Mapean métodos HTTP a funciones del controller.

| Anotación | Método HTTP |
|-----------|-------------|
| `@GetMapping` | GET |
| `@PostMapping` | POST |
| `@PutMapping` | PUT |
| `@DeleteMapping` | DELETE |

```java
@GetMapping("/{id}")
public ResponseEntity<FruitDto> getFruitById(@PathVariable Long id) { ... }
```

### @PathVariable
**Ubicación:** `FruitController.java`

Extrae variables de la URL (ej: `{id}` en `/fruits/{id}`).

### @RequestBody
**Ubicación:** `FruitController.java`

Convierte el cuerpo de la petition HTTP (JSON) a un objeto Java.

### @Valid
**Ubicación:** `FruitController.java`

Activa la validación de objetos usando Jakarta Validation (anotaciones como `@NotBlank`, `@Positive`, etc).

---

## 3. Anotaciones de JPA / Hibernate

### @Entity
**Ubicación:** `Fruit.java`, `Provider.java`

Marca la clase como una entidad JPA que se persistirá en la base de datos.

### @Table
**Ubicación:** `Provider.java`

Define el nombre de la tabla en la base de datos (opcional si el nombre de la clase coincide).

```java
@Table(name = "providers")
public class Provider { ... }
```

### @Id
**Ubicación:** `Fruit.java`, `Provider.java`

Marca el campo como la clave primaria de la entidad.

### @GeneratedValue
**Ubicación:** `Fruit.java`, `Provider.java`

Configura la generación automática del ID.

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Estrategias:**
- `IDENTITY` - Auto-increment en la BD
- `SEQUENCE` - Secuencia de BD
- `TABLE` - Tabla de генерации
- `AUTO` - Spring decide automáticamente

### @ManyToOne
**Ubicación:** `Fruit.java`

Define una relación muchos-a-uno con otra entidad.

```java
@ManyToOne
@JoinColumn(name = "provider_id")
private Provider provider;
```

### @JoinColumn
**Ubicación:** `Fruit.java`

Especifica la columna de unión (foreign key) en la tabla.

### @OneToOne, @OneToMany, @ManyToMany
Relaciones JPA adicionales (no usadas actualmente):
- `@OneToOne` - Uno a uno
- `@OneToMany` - Uno a muchos
- `@ManyToMany` - Muchos a muchos

---

## 4. Anotaciones de Validación (Jakarta Validation)

### @NotBlank
**Ubicación:** `FruitDto.java`, `ProviderDto.java`

El campo String no puede ser null ni estar vacío (""). Genera error 400 si falla.

```java
@NotBlank(message = "Name is required")
private String name;
```

### @NotNull
**Ubicación:** `FruitDto.java`

El campo no puede ser null. Funciona con cualquier tipo.

```java
@NotNull(message = "Weight is required")
private int weightInKilos;
```

### @Positive
**Ubicación:** `FruitDto.java`

El número debe ser mayor que 0.

```java
@Positive(message = "Weight must be positive")
private int weightInKilos;
```

### Otras anotaciones de validación (no usadas):

| Anotación | Descripción |
|-----------|-------------|
| `@NotEmpty` | No null y no vacío (colecciones) |
| `@Size` | Tamaño mínimo/máximo |
| `@Min` / `@Max` | Valor mínimo/máximo |
| `@Email` | Formato de email válido |
| `@Pattern` | Expresión regular |
| `@Past` / `@Future` | Fecha en pasado/futuro |

---

## 5. Anotaciones de Componentes Spring

### @Service
**Ubicación:** `FruitService.java`

Marca la clase como componente de servicio (lógica de negocio).

```java
@Service
public class FruitService { ... }
```

### @Repository
**Ubicación:** `FruitRepository.java`

Marca la clase como componente de repositorio (acceso a datos).

```java
@Repository
public interface FruitRepository extends JpaRepository<Fruit, Long> { ... }
```

### @RestControllerAdvice
**Ubicación:** `GlobalExceptionHandler.java`

Combina `@ControllerAdvice` + `@ResponseBody`. Maneja excepciones globalmente para todos los Controllers REST.

### @ExceptionHandler
**Ubicación:** `GlobalExceptionHandler.java`

Define qué tipo de excepción maneja cada método.

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFound(...) { ... }
```

---

## 6. Anotaciones de Lombok

Lombok genera código automáticamente en tiempo de compilación.

### @Getter
**Ubicación:** `Fruit.java`, `Provider.java`

Genera getters para todos los campos.

```java
@Getter
public class Fruit { ... }
```

### @Setter
**Ubicación:** `Fruit.java`, `Provider.java`

Genera setters para todos los campos.

### @Data
**Ubicación:** `FruitDto.java`

Combina `@Getter` + `@Setter` + `@ToString` + `@EqualsAndHashCode`.

### @NoArgsConstructor
**Ubicación:** `FruitDto.java`

Genera constructor sin argumentos.

### @AllArgsConstructor
**Ubicación:** `FruitDto.java`

Genera constructor con todos los argumentos.

---

## Resumen Visual

```
@SpringBootApplication
        │
        ├── @SpringBootConfiguration
        ├── @EnableAutoConfiguration
        └── @ComponentScan

@RestController
        │
        ├── @Controller
        └── @ResponseBody

@Entity
        │
        ├── @Table
        ├── @Id
        ├── @GeneratedValue
        ├── @ManyToOne / @OneToMany / @ManyToMany
        └── @JoinColumn

@Service / @Repository / @RestControllerAdvice
        │
        └──@Component (base)

@Data / @Getter / @Setter / @NoArgsConstructor / @AllArgsConstructor
        │
        └── Lombok (generación de código)

@NotBlank / @NotNull / @Positive / @Size / @Email ...
        │
        └── Jakarta Validation (Bean Validation)
```