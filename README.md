# Student Management System (Spring Boot REST demo)

Stack: Java 17, Spring Boot 3.4, Spring Data JPA (Hibernate), MySQL 8, Maven.

## Run
1. Start MySQL. The database `student_db` is created automatically.
2. Edit `src/main/resources/application.yml` (username/password) or set env var `DB_PASSWORD`.
3. IntelliJ IDEA: File > Open > select this folder (pom.xml) > wait for Maven import > run `StudentManagementApplication`.
4. Open Swagger UI: http://localhost:8080/swagger-ui.html
5. Try requests in `api-test.http` (IntelliJ's built-in HTTP client, click the green run icon).

## REST API
| Method | URL | Description |
|---|---|---|
| GET | /api/students?keyword=&page=&size=&sortBy=&direction= | paged list / search |
| GET | /api/students/{id} | get one |
| POST | /api/students | create (201) |
| PUT | /api/students/{id} | update |
| DELETE | /api/students/{id} | delete (204) |
| POST | /api/students/{sid}/courses/{cid} | enroll |
| DELETE | /api/students/{sid}/courses/{cid} | drop |
| GET/POST/PUT/DELETE | /api/courses[/{id}] | course CRUD |

Errors: 400 validation (with `fieldErrors`), 404 not found, 409 duplicate.

## Layers
controller (HTTP) -> service (business logic + transactions) -> repository (Spring Data JPA) -> entity (Hibernate mapping to MySQL tables)
