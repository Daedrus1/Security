<h1 align="center">📚 Security — Book Store REST API</h1>

<p align="center">
  A Spring Boot REST API for a simple online bookstore with JWT authentication, role-based access,
  shopping cart functionality, and order management.
</p>

<p align="center">
  <a href="https://github.com/Daedrus1/Security">Repository</a> •
  <a href="#-getting-started">Getting Started</a> •
  <a href="#-swagger--api-documentation">Swagger</a> •
  <a href="#-docker--mysql">Docker</a> •
  <a href="#-tests">Tests</a>
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-blue" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen" />
  <img alt="Spring Security" src="https://img.shields.io/badge/Spring%20Security-JWT-orange" />
  <img alt="Liquibase" src="https://img.shields.io/badge/Liquibase-enabled-yellow" />
  <img alt="Swagger" src="https://img.shields.io/badge/Swagger-OpenAPI-green" />
  <img alt="Build Tool" src="https://img.shields.io/badge/Maven-build-informational" />
</p>

---

##  About the project

**Security** is a backend learning project that simulates a small **online bookstore**.

The main goal of this project was to practice building a **secure REST API** using Spring Boot
and to work with features that are commonly used in real backend applications:
authentication, authorization, database migrations, validation, documentation, and testing.

The project follows a typical e-commerce flow:

**books → shopping cart → orders**,  
with role-based access control and JWT authentication.

---

##  Features

- JWT-based authentication (login → access token)
- Role-based authorization (`USER` / `ADMIN`)
- Books management (CRUD)
- Categories management (CRUD)
- Shopping cart functionality (add, update, remove items)
- Order creation from cart
- Order history and order items
- DTO layer with validation
- Global exception handling with clean API responses
- Database migrations using Liquibase
- Interactive API documentation with Swagger (OpenAPI)

---

##  Tech stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Web**
- **Spring Security + JWT**
- **Spring Data JPA (Hibernate)**
- **Liquibase**
- **H2 (in-memory database)**
- **MySQL**
- **MapStruct**
- **Lombok**
- **Swagger / OpenAPI (springdoc)**
- **Maven**

---

##  Project structure

The project follows a typical layered architecture:

- `controller` — REST API endpoints
- `service` — business logic
- `repository` — database access
- `dto` — request and response models
- `mapper` — MapStruct mappers
- `security` — JWT logic and authentication
- `config` — Spring configuration
- `db/changelog` — Liquibase migration files

This structure helps keep the code clean and easy to maintain.

---

##  Getting started

### Requirements

- Java 17 or higher
- Maven

---

### Run locally (H2 database)

```bash
git clone https://github.com/Daedrus1/Security.git
cd Security
mvn spring-boot:run
The application will start on:

http://localhost:8081
📘 Swagger / API documentation
Swagger UI is available at:

http://localhost:8081/swagger-ui/index.html
How to test secured endpoints
Register a new user

Login to receive a JWT token

Click Authorize in Swagger

Paste the token in the format:

Bearer <your_token>
🗃️ Database
Default configuration (H2)
H2 Console:

http://localhost:8081/h2-console
Credentials (from application.properties):

JDBC URL: jdbc:h2:mem:testdb

Username: root

Password: Pablo1234

Database migrations (Liquibase)
Liquibase runs automatically on application startup:

classpath:/db/changelog/db.changelog-master.yaml
ddl-auto=validate is used to prevent unintended schema changes.

🐳 Docker + MySQL
The project can be run with MySQL using Docker.

Create a .env file in the project root:

MYSQLDB_ROOT_PASSWORD=rootpass
MYSQLDB_DATABASE=security_db
MYSQLDB_USER=security_user
MYSQLDB_PASSWORD=security_pass

MYSQLDB_HOST=mysql
MYSQLDB_PORT=3306

APP_PORT=8081
Start containers:

docker compose up --build
Stop containers:

docker compose down
🔐 JWT configuration
JWT settings are located in application.properties:

jwt.secret=change_me_super_secret_key_256_bits_min
jwt.expiration=3600000
Use the token in requests:

Authorization: Bearer <JWT_TOKEN>
🧪 Tests
Run tests using:

mvn test
🎥 Loom demo (4 minutes)
Loom video demonstrating the project:

https://www.loom.com/share/25911a2ded1047c3b360d8c9722a64e1
In the demo, I show:

Swagger UI overview

Authentication flow (register / login)

Access to secured endpoints

Typical flow: books → cart → order

👤 Author
Pavlo Mykhailyk
GitHub: https://github.com/Daedrus1