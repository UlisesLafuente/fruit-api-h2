# TODO - Fruit API H2

## Configuración del Proyecto
- [x] Generar proyecto Spring Boot en start.spring.io
- [x] Configurar Maven/Gradle con dependencias: Spring Boot DevTools, Spring Web, Spring Data JPA, H2 Database, Validation, Lombok
- [x] Configurar Java 21 y Spring Boot 3.x

## Estructura de Paquetes
- [x] Crear paquete `cat.itacademy.s04.t02.n01.controllers`
- [x] Crear paquete `cat.itacademy.s04.t02.n01.model`
- [x] Crear paquete `cat.itacademy.s04.t02.n01.services`
- [x] Crear paquete `cat.itacademy.s04.t02.n01.repository`
- [x] Crear paquete `cat.itacademy.s04.t02.n01.exception`

## Entity Fruit
- [x] Crear entidad Fruit con propiedades: id, name, weightInKilos
- [x] Configurar @Id y @GeneratedValue para generación automática de ID

## DTOs
- [x] Crear FruitDto para entrada/salida de datos
- [x] Aplicar anotaciones de validación: @NotBlank, @NotNull, @Positive
- [x] Implementar mapeo entre Entity y DTO

## Repository
- [x] Crear FruitRepository extendiendo JpaRepository

## Services
- [x] Crear FruitService con lógica de negocio
- [x] Implementar métodos: addFruit, getAllFruits, getFruitById, updateFruit, deleteFruit
- [x] Escribir tests unitarios con Mockito

## Controller
- [x] Crear FruitController con endpoints REST
- [x] Implementar POST /fruits - Create fruit
- [x] Implementar PUT /fruits/{id} - Update fruit
- [x] Implementar DELETE /fruits/{id} - Remove by id
- [x] Implementar GET /fruits/{id} - Get fruit by id
- [x] Implementar GET /fruits - Get all fruits
- [x] Usar ResponseEntity para devolver códigos HTTP correctos

## Exception Handling
- [x] Crear GlobalExceptionHandler para manejo centralizado de excepciones
- [x] Implementar manejo de validation errors (HTTP 400)
- [x] Implementar manejo de ResourceNotFoundException (HTTP 404)
- [x] Configurar respuestasJson consistentes para errores

## Tests (TDD)
- [x] Escribir tests de integración con @SpringBootTest y MockMvc
- [x] Escribir tests unitarios para servicios con Mockito
- [x] Verificar códigos de respuesta HTTP correctos (201, 200, 204, 400, 404)

## Docker
- [x] Crear Dockerfile multi-stage
- [x] Stage 1: Compilar aplicación y generar .jar
- [x] Stage 2: Copiar .jar a imagen ligera para producción
- [x] Configurar connection a base de datos mediante environment variables

## Configuración
- [x] Configurar conexión a H2 mediante environment variables
- [x] Verificar que la aplicación funcione correctamente