Description

In this task you will develop three independent Spring Boot applications, each with a REST API that implements a complete CRUD (Create, Read, Update, Delete) on different entities. You will work with three different databases : H2, MySQL and MongoDB.

Through these practices you will learn to:

    » Create REST APIs using Spring Boot.
    » Manage data persistence with Spring Data JPA and Spring Data MongoDB.
    » Apply HTTP verbs correctly (GET, POST, PUT, DELETE) and properly manage the status codes of the answers.
    » Implement dynamic routes with Path Params and Query Params.
    » Write and run automated tests using TDD (Test-Driven Development) to ensure the expected behavior of logic and endpoints.
    » Manage exceptions globally by one GlobalExceptionHandler.
    » Structure the project correctly following the MVC (Model-View-Controller) pattern.
    » Create relationships between entities using JPA.
    » Enter the use of DTOs and validate the input data with validation annotations.
    » Create a Dockerfileto pack the application in a Docker image prepared for production environments.
    » Configure the connection to the database through environment variables.

Level 1
CRUD exercise with H2

In this first level you will develop a REST API to manage the stock of a greengrocer through a backend application built with Spring Boot.

The objective is to manage the entry of fruit stock, recording for each one the name of the product and its weight in kilos.

You will work with a memory (H2) SQL database, widely used in development and testing environments for its speed and simplicity of configuration.
User Stories and Acceptance Criteria

⚠️ Et recomanem fer un seguiment de cada una de les següents
històries d'usuari utilitzant un tauler Kanban (com GitHub Projects,
Trello, etc.). A més, és bona pràctica fer un commit clar i descriptiu
un cop completada cada història.

1. Register a new fruit

   Com aAs responsible for the inventory,
   I want to be able to add a new fruit entry indicating your name and weight in kilos,
   in order to keep an updated record of the incoming product.

Acceptance criteria:

    If the data is valid, the system returns HTTP 201 Created with the fruit detail.
    If the name is empty or the weight is invalid, HTTP 400 Bad Request is returned.

2. 2. Check all the fruits

Com aAs responsible for the inventory,
I want to be able to view a list with all the registered fruits,
per tal deto have a global view of the available stock.

Acceptance criteria:

    The system returns HTTP 200 OK and a JSON array with all the fruits.
    If there are no registered fruits, return an empty array with HTTP 200 OK.

3. Consult a specific fruit

   Com aAs responsible for the inventory,
   I want to be able to consult the details of a specific fruit from its identifier,
   per tal deto access information on a specific product efficiently.

Acceptance criteria:

    If the ID exists, the system returns HTTP 200 OK with the detail of the fruit.
    If the ID does not exist, return HTTP 404 Not Found with an indicative message.

4. Modify an existing fruit

   Com aAs responsible for the inventory,
   I want to be able to update the name or the recorded weight of a fruit,
   to correct errors or reflect changes in product information.

Acceptance criteria:

    If the data is valid, the system returns HTTP 200 OK with the updated fruit.
    If the ID does not exist, return HTTP 404 Not Found.
    If the data is invalid, return HTTP 400 Bad Request.

5. Eliminate a fruit

   Com aAs responsible for the inventory,
   I want to be able to remove a fruit from its identifier,
   per tal deto ensure that the stock only contains relevant and up-to-date information.

Acceptance criteria:

    If the ID exists, the system removes the fruit and returns HTTP 204 No Content.
    If the ID does not exist, the system returns HTTP 404 Not Found with an error message.

Project Configuration

Access https://start.spring.io/ and generate a Spring Boot project with the following characteristics:
Parameter 	Value
PROJECT 	Maven or Gradle
LANGUAGE 	Java
SPRING BOOT 	3.x.x (latest stable)
Group 	cat.itacademy.s04.t02.n01
Artifact / Name 	fruit-api-h2
Description 	REST API for managing fruit stock with H2
Package name 	cat.itacademy.s04.s02.n01.fruit
PACKAGING 	Jar
JAVA 	21 (LTS)
Dependencies

    Spring Boot DevTools
    Spring Web
    Spring Data JPA
    H2 Database
    Validation
    Lombok (optional, but recommended to reduce boilerplate code)

Technical statement

You will work with an entity called Fruit, which will have the following properties:
Fruit

Long id  
String name  
int weightInKilos

Taking advantage of the JPA specification, you will have to persist this entity in an H2 database, following the MVC architecture.

Remember that JPA will be responsible for automatically generating the table and the ID values for each fruit, using the annotation @Idtogether with @GeneratedValue.

Organize the project by creating the following packages, according to your main package:

cat.itacademy.s04.t02.n01.controllers
cat.itacademy.s04.t02.n01.model
cat.itacademy.s04.t02.n01.services
cat.itacademy.s04.t02.n01.repository
cat.itacademy.s04.t02.n01.exception

The class located within the package controllers(FruitController, for example) will be able to manage the following operations through endpointsREST endpoints:
Expected Endpoints
Method 	Endpoint 	Description
POST 	/fruits 	Create fruit
PUT 	/fruits/{id} 	Update fruit
DELETE 	/fruits/{id} 	Remove by id
GET 	/fruits/{id} 	Get a fruit per id
GET 	/fruits 	Get all the fruits

Important

    You will have to take into account the good design practices of the APIs, using correctly the error codes and the answers in case of incorrect invocations. (You can find information about ResponseEntity.)

    You should avoid directly exposing JPA entities to controllers, using the DTO pattern to manage the input and output data, and validating them with Bean Validation annotations such as @NotBlank, @NotNullor @Positive.

    You will need to implement a GlobalExceptionHandler to manage the exceptions globally in the application. This will allow to capture and treat errors in a centralized way, improving robustness and coherence in the management of exceptions.

    Using a generative AI, you will need to create a Dockerfilefor the project that allows to build an optimized image for production environments. The objective is to understand line by line how an image is generated through a multi-stage build divided into two stages:
        Construction stage: compile the application and generate the file .jar.
        Final stage: copy only the .jarto a light image to run it in production.

    You will have to develop the project following the TDD (Test-Driven Development) approach. That is, before implementing each functionality, you will have to write the corresponding test that defines the expected behavior. You can use:
        @SpringBootTestwith MockMvc, or bookshops like RestAssured, to try the REST endpoints.
        Mockitoto test the services in isolation (unit test).

