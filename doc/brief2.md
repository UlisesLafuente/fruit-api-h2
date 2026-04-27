you will expand the functionality of the previous application by incorporating the management of fruit suppliers (you can start from what you already had at Level 1). Each fruit record must be associated with a supplier, which will allow you to register the origin of each product and check which fruits each company supplies.

This new project will use MySQL as a database and introduce a relationship between entities through JPA, specifically a @ManyToOne type association between Fruitand Provider.
User stories and acceptance criteria
1. Registering a supplier

   Com aAs a purchasing manager,
   I want to be able to add new suppliers indicating their name and country,
   in order to keep track of who supplies the fruits.

Acceptance criteria:

    The system must allow you to register suppliers with name and country.
    No suppliers with the same name or empty name can be registered.
    If the provider has registered correctly, HTTP 201 Created is returned.

2. 2. Add a fruit with supplier

Com aAs a purchasing manager,
I want to add a new fruit associated with an existing supplier,
to correctly record the origin of each product.

Acceptance criteria:

    When creating a fruit, it is necessary to indicate the ID of a valid supplier.
    Fruits cannot be added without a supplier.
    If the provider does not exist, it is returned HTTP 404 Not Found.
    If the data is valid, return HTTP 201 Created.

3. Filter fruits by a supplier

   As a stock manager,
   I want to be able to see all the fruits supplied by a supplier,
   in order to track your supply.

Acceptance criteria:

    The system should allow you to consult fruits filtering by supplier ID.
    If the provider exists, it is returned HTTP 200 OK with the fruits.
    If it does not exist, it is returned HTTP 404 Not Found.

4. Update a provider

   Com aAs a purchasing manager, I want to be able to update the information of a supplier, in order to keep the correct and updated data.

Acceptance criteria:

    If the supplier’s ID exists, the name and country can be updated.
    If the data is valid, HTTP 200 OK is returned with the updated provider.
    If the ID does not exist, it is returned HTTP 404 Not Found.
    Suppliers with the empty name or that already exists cannot be updated.

5. Remove a supplier

   As a purchasing manager, I want to be able to remove a supplier, in order to keep the database clean and up-to-date.

Acceptance criteria:

    If the supplier’s ID exists and does not have associated fruits, it can be removed.
    If the removal is correct, it is returned HTTP 204 No Content.
    If the ID does not exist, it is returned HTTP 404 Not Found.
    If the supplier has associated fruits, it cannot be removed and HTTP 400 Bad Request is returned with an indicative message.

Project Configuration

Access https://start.spring.io/ and generate a Spring Boot project with the following characteristics:
Parameter 	Value
PROJECT 	Maven or Gradle
LANGUAGE 	Java
SPRING BOOT 	3.x.x (latest stable)
Group 	cat.itacademy.s04.t02.n02
Artifact / Name 	fruit-api-MySQL
Description 	REST API for managing fruit stock with MySQL
Package name 	cat.itacademy.s04.t02.n02.fruit
PACKAGING 	Jar
JAVA 	21 (LTS)
Dependencies

    Spring Boot DevTools
    Spring Web
    Spring Data JPA
    MySQL Driver
    Validation
    Lombok (optional)

Technical statement

You will work with two related entities:
Provider

Long id  
String name  
String country

Fruit

Long id  
String name  
int weightInKilos  
Provider provider

You must persist these entities to a MySQL database, managing the relationship with JPA (@ManyToOne).
Minimum expected Endpoints
Method 	Endpoint 	Description
POST 	/providers 	Create provider
GET 	/providers 	List suppliers
PUT 	/providers/{id} 	Update provider
GET 	/fruits?providerId={id} 	Get fruits from a supplier
DELETE 	/providers/{id} 	Remove provider

In addition to the new supplier-related endpoints, all Level 1 endpoints must continue to work properly with the new data structure.

Important

    Make sure you also comply with all the non-functional requirements established in Level 1.
    Use DTOs to manage the input and output information, avoiding directly exposing the model entities.
    Apply validations on the fields of the DTOs using annotations such as @NotBlank, @Positiveor @NotNull, supported by the Spring Validation Library.
    Using a Generative AI, it creates a Dockerfileto pack the application in a production-optimized Docker image, allowing the configuration of the connection to the MySQL database using environment variables.
    To facilitate the development environment, add a compose docker file to lift the necessary infrastructure, such as the MySQL database service.
    Opcional:Optional: You can complement the task with endpoint integration tests using @SpringBootTestand MockMvc, or/and unitary tests of services with Mockito.

