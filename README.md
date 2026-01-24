<h1 align="center">📚 Security — Book Store REST API</h1>

<p align="center">
  Spring Boot REST API for a simple online bookstore with JWT auth, role-based access, shopping cart and orders.
</p>

<p align="center">
  <a href="https://github.com/Daedrus1/Security">Repository</a> •
  <a href="#-getting-started">Getting Started</a> •
  <a href="#-swagger--api-docs">Swagger</a> •
  <a href="#-docker--mysql">Docker</a> •
  <a href="#-tests">Tests</a>
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-blue" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen" />
  <img alt="Security" src="https://img.shields.io/badge/Spring%20Security-JWT-orange" />
  <img alt="Liquibase" src="https://img.shields.io/badge/Liquibase-enabled-yellow" />
  <img alt="Swagger" src="https://img.shields.io/badge/Swagger-OpenAPI-green" />
  <img alt="Build" src="https://img.shields.io/badge/Maven-build-informational" />
</p>

---

## ✨ About

**Security** is a backend project that simulates a small **book store**.  
It covers the things that реально встречаются в backend-разработке: авторизация, работа с БД, миграции, валидация, документация, тесты и аккуратная структура проекта.

Main idea: **secure REST API** + typical e-commerce flow (books → cart → orders).

---

## 🧩 Features

- ✅ JWT Authentication (login → access token)
- ✅ Role-based authorization (USER / ADMIN)
- ✅ Books management (CRUD)
- ✅ Categories management (CRUD)
- ✅ Shopping cart logic (add/update/remove items)
- ✅ Orders (create from cart, order history, order items)
- ✅ DTO layer + validation
- ✅ Global error handling (clean responses)
- ✅ Database migrations with Liquibase
- ✅ Interactive Swagger documentation

---

## 🧰 Tech stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Web**
- **Spring Security + JWT (jjwt)**
- **Spring Data JPA (Hibernate)**
- **Liquibase**
- **H2 (in-memory) / MySQL**
- **MapStruct**
- **Lombok**
- **Swagger/OpenAPI (springdoc)**
- **Maven**

---

## 🗂️ Project structure

Typical layered architecture:

- `controller` — API endpoints
- `service` — business logic
- `repository` — data access
- `dto` — request/response models
- `mapper` — MapStruct mappers
- `security` — JWT + user auth
- `config` — Spring configuration
- `db/changelog` — Liquibase migrations

---

## ✅ Getting started

### Requirements
- Java 17+
- Maven

### Run locally (H2)
```bash
git clone https://github.com/Daedrus1/Security.git
cd Security
mvn spring-boot:run
App will start on:

http://localhost:8081

📘 Swagger / API docs
Swagger UI:

http://localhost:8081/swagger-ui/index.html

To test secured endpoints:

register/login

copy token

click Authorize in Swagger

paste:

Bearer <your_token>
🗃️ Database
Default (H2)
H2 Console:

http://localhost:8081/h2-console

Credentials (from application.properties):

JDBC URL: jdbc:h2:mem:testdb

username: root

password: Pablo1234

Migrations (Liquibase)
Liquibase runs on application startup:

classpath:/db/changelog/db.changelog-master.yaml

ddl-auto=validate is used to avoid silent schema changes.

🐳 Docker + MySQL
The project supports MySQL configuration via environment variables.

Create .env file in project root:

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
🔐 JWT settings
Located in application.properties:

jwt.secret=change_me_super_secret_key_256_bits_min
jwt.expiration=3600000
Use token in requests:

Authorization: Bearer <JWT_TOKEN>
🧪 Tests
Run:

mvn test
🎥 Loom demo (2–4 min)
Loom video:

<PASTE_YOUR_LOOM_LINK_HERE>

In the demo I show:

Swagger UI

auth flow (register/login)

access to secured endpoints

typical flow: books → cart → order

👤 Author
Pavlo Mykhailyk
GitHub: https://github.com/Daedrus1