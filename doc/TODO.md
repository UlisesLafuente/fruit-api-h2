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
- [ ] Crear entidad Fruit con propiedades: id, name, weightInKilos
- [ ] Configurar @Id y @GeneratedValue para generación automática de ID

## DTOs
- [ ] Crear FruitDto para entrada/salida de datos
- [ ] Aplicar anotaciones de validación: @NotBlank, @NotNull, @Positive
- [ ] Implementar mapeo entre Entity y DTO

## Repository
- [ ] Crear FruitRepository extendiendo JpaRepository

## Services
- [ ] Crear FruitService con lógica de negocio
- [ ] Implementar métodos: addFruit, getAllFruits, getFruitById, updateFruit, deleteFruit
- [ ] Escribir tests unitarios con Mockito

## Controller
- [ ] Crear FruitController conendpoints REST
- [ ] Implementar POST /fruits - Create fruit
- [ ] Implementar PUT /fruits/{id} - Update fruit
- [ ] Implementar DELETE /fruits/{id} - Remove by id
- [ ] Implementar GET /fruits/{id} - Get fruit by id
- [ ] Implementar GET /fruits - Get all fruits
- [ ] Usar ResponseEntity para devolver códigos HTTP correctos

## Exception Handling
- [ ] Crear GlobalExceptionHandler para manejo centralizado de excepciones
- [ ] Implementar manejo de validation errors (HTTP 400)
- [ ] Implementar manejo de ResourceNotFoundException (HTTP 404)
- [ ] Configurar respuestasJson consistentes para errores

## Tests (TDD)
- [ ] Escribir tests de integración con @SpringBootTest y MockMvc
- [ ] Escribir tests unitarios para servicios con Mockito
- [ ] Verificar códigos de respuesta HTTP correctos (201, 200, 204, 400, 404)

## Docker
- [ ] Crear Dockerfile multi-stage
- [ ] Stage 1: Compilar aplicación y generar .jar
- [ ] Stage 2: Copiar .jar a imagen ligera para producción
- [ ] Configurar connection a base de datos mediante environment variables

## Configuración
- [ ] Configurar conexión a H2 mediante environment variables
- [ ] Verificar que la aplicación funcione correctamente