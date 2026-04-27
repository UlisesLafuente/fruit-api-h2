# TODO - Fruit API MySQL (Nivel 2)

## 1. Configuración del Proyecto

- [x] Generar proyecto Spring Boot en start.spring.io con dependencias: Spring Boot DevTools, Spring Web, Spring Data JPA, MySQL Driver, Validation, Lombok
- [x] Actualizar groupId a `cat.itacademy.s04.t02.n02`
- [x] Actualizar artifactId a `fruit-api-MySQL`
- [x] Configurar Java 21 y Spring Boot 3.x (ya configurado en pom.xml)
- [x] Cambiar dependencia H2 por MySQL Driver

## 2. Configuración de Base de Datos MySQL

- [x] Crear docker-compose.yml con servicio MySQL
- [x] Configurar MySQL con volumen para persistencia
- [x] Crear base de datos `fruit_db` y usuario
- [x] Actualizar application.properties para MySQL
- [x] Configurar connection mediante environment variables

## 3. Entity Provider

- [x] Crear paquete `cat.itacademy.s04.t02.n02.fruit.model`
- [x] Crear entidad Provider con propiedades: id, name, country
- [x] Configurar @Id y @GeneratedValue para generación automática de ID
- [x] Actualizar entity Fruit para incluir relación @ManyToOne con Provider

## 4. DTOs

- [x] Crear FruitDto para entrada/salida de datos (ya existe)
- [x] Aplicar anotaciones de validación: @NotBlank, @NotNull, @Positive (ya existe en FruitDto)
- [x] Implementar mapeo entre Entity y DTO (ya existe en FruitDto)
- [x] Crear ProviderDto para entrada/salida de datos
- [x] Aplicar anotaciones de validación: @NotBlank
- [x] Implementar mapeo entre Entity y DTO (Provider ↔ ProviderDto)
- [x] Actualizar FruitDto para incluir providerId
- [x] Crear FruitRequestDto con providerId para crear/actualizar frutas (simplificado: FruitDto actualizado)

## 5. Repository

- [x] Crear FruitRepository extendiendo JpaRepository (ya existe)
- [x] Crear ProviderRepository extendiendo JpaRepository
- [x] Añadir método para buscar provider por nombre (existsByName)

## 6. Provider Service

- [x] Crear ProviderService con lógica de negocio
- [x] Implementar método: addProvider (validar nombre único)
- [x] Implementar método: getAllProviders
- [x] Implementar método: getProviderById
- [x] Implementar método: updateProvider
- [x] Implementar método: deleteProvider (verificar si tiene frutas asociadas)
- [x] Implementar método: getFruitsByProviderId

## 7. Provider Controller

- [x] Crear ProviderController con endpoints REST
- [x] Implementar POST /providers - Create provider (201)
- [x] Implementar GET /providers - List all providers (200)
- [x] Implementar GET /providers/{id} - Get provider by id (200/404)
- [x] Implementar PUT /providers/{id} - Update provider (200/404/400)
- [x] Implementar DELETE /providers/{id} - Remove provider (204/404/400)
- [x] Usar ResponseEntity para devolver códigos HTTP correctos

## 8. Actualizar Fruit Controller

- [x] Actualizar POST /fruits para aceptar providerId en el cuerpo
- [x] Implementar GET /fruits?providerId={id} - Filter fruits by provider
- [x] Validar que el provider existe al crear/actualizar fruit

## 9. Exception Handling

- [x] Crear ResourceNotFoundException (ya existe)
- [x] Crear ErrorResponse (ya existe)
- [x] Crear GlobalExceptionHandler (ya existe)
- [x] Crear ProviderNotFoundException
- [x] Crear ProviderAlreadyExistsException
- [x] Crear ProviderHasFruitsException (para cuando se intenta eliminar un provider con frutas)
- [x] Actualizar GlobalExceptionHandler para manejar nuevas excepciones
- [x] Mantener manejo de validation errors (HTTP 400) (ya existe)
- [x] Mantener manejo de ResourceNotFoundException (HTTP 404) (ya existe)

## 10. Tests

- [x] Tests unitarios con Mockito
- [x] Actualizar tests unitarios de FruitService para nueva estructura
- [x] Crear tests unitarios de ProviderService
- [x] Crear tests de integración de ProviderController
- [x] Actualizar tests de FruitControllerIntegrationTest
- [x] Verificar códigos de respuesta HTTP correctos (201, 200, 204, 400, 404)

## 11. Docker

- [x] Dockerfile multi-stage (ya existe, pero para H2)
- [x] Actualizar Dockerfile para MySQL (no H2)
- [x] Configurar environment variables para conexión MySQL
- [x] Crear docker-compose.yml con servicio MySQL
- [x] Verificar que docker-compose.yml funciona correctamente

## 12. Validación de Requisitos No Funcionales (Nivel 1)

- [x] DTOs con validaciones ✅
- [x] Manejo de excepciones centralizado ✅
- [x] Respuestas JSON consistentes ✅
- [x] Tests unitarios ✅
- [x] Tests de integración ✅

## Notas

### Nuevos Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /providers | Crear proveedor |
| GET | /providers | Listar todos los proveedores |
| GET | /providers/{id} | Obtener proveedor por ID |
| PUT | /providers/{id} | Actualizar proveedor |
| DELETE | /providers/{id} | Eliminar proveedor |
| GET | /fruits?providerId={id} | Filtrar frutas por proveedor |

### Entidades

**Provider**
- Long id
- String name
- String country

**Fruit** (actualizado)
- Long id
- String name
- int weightInKilos
- Provider provider (@ManyToOne)

### Relaciones

```
Provider (1) ←──── (N) Fruit
@OneToMany          @ManyToOne
```

Un Provider puede tener muchos Fruits.
Un Fruit pertenece a un Provider.