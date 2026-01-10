# Task Manager API (Spring Boot)
[![Java CI](https://github.com/MerulG/Task-Manager/actions/workflows/ci.yml/badge.svg)](https://github.com/MerulG/Task-Manager/actions/workflows/ci.yml)

A production-ready **Task Manager REST API** built with **Spring Boot**, featuring CRUD operations, validation, error handling, database integration, Docker support, CI/CD, and cloud deployment.

---

## Features

### Spring Boot & REST API Fundamentals
- Full CRUD for Tasks
- REST endpoints using `@RestController`
- Layered architecture (Controller → Service → Repository)
- Input validation using `@NotNull`, `@Size`, etc.
- Global exception handling using `@ControllerAdvice`
- Swagger/OpenAPI documentation
- Postman-tested API behaviour

### Database Integration (JPA + MySQL/Postgres)
- Spring Data JPA with Hibernate
- MySQL/Postgres database integration
- User ↔ Task relationship (`@OneToMany`, `@ManyToOne`)
- Custom queries (e.g., find tasks by status or user ID)
- Pagination & sorting (`Pageable`)
- Seed data using `data.sql`
- Pre-made users with hashed passwords:
    - **Alice (USER)** – username: `alice`, password: `password123`
    - **Bob (USER)** – username: `bob`, password: `password123`
    - **Admin (ADMIN)** – username: `admin`, password: `admin123`
- Improved validation and error handling

### Testing, Docker & Deployment
- Unit tests using **JUnit 5** and **Mockito**
- Repository slicing tests with `@DataJpaTest`
- Dockerfile for containerizing the application
- `docker-compose` for app + database
- GitHub Actions CI pipeline (build + tests)
- Cloud deployment (Render)

### Security (Spring Security + JWT)
- Stateless authentication using JWT
- User registration and login endpoints
- BCrypt password hashing
- Role-based access control (USER / ADMIN)
- Custom JWT authentication filter
- Secured endpoints with method-level authorization

---

## Tech Stack

### **Backend**
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT

### **Database**
- PostgreSQL
- schema auto-generation (`ddl-auto`)

### **DevOps**
- Docker
- Docker Compose
- GitHub Actions (CI)
- Render.com

### **Testing**
- JUnit 5
- Mockito
- MockMvc

### **Documentation & Swagger**
- Swagger / springdoc-openapi
- Swagger UI: [https://task-manager-wc0c.onrender.com/swagger-ui/index.html](https://task-manager-wc0c.onrender.com/swagger-ui/index.html)

---

## Logging into Swagger UI with JWT

1. Open the **Swagger UI** link.
2. Find the `POST /api/auth/login` endpoint.
3. Click **Try it out** and enter the username and password of one of the pre-made users:
    - **Alice (USER)** – username: `alice`, password: `password123`
    - **Bob (USER)** – username: `bob`, password: `password123`
    - **Admin (ADMIN)** – username: `admin`, password: `admin123`
4. Click **Execute** to receive a JWT token in the response.
5. Copy the `token` from the response.
6. Click the **Authorize** button (top-right in Swagger UI) → **Bearer Token**, and paste the copied token.
7. Now all endpoints will use your JWT, and only those allowed for your role will succeed.

> ⚠️ Note: Users will only be able to access their own tasks unless they are an Admin, use the admin account to find the IDs of other users and tasks


---

### **Endpoint Access by Role**

#### **Admin-only Endpoints**
- Delete any user or task (`DELETE /api/users/{id}`, `DELETE /api/tasks/{id}`)
- Update any user or task (`PUT /api/users/{id}`, `PUT /api/tasks/{id}`)
- View all users (`GET /api/users`)

#### **User Endpoints**
- Retrieve tasks assigned to themselves (`GET /api/tasks/user/{userId}`)
- Retrieve tasks by status or title for themselves (`GET /api/tasks/user/{userId}/status/{status}`, `GET /api/tasks/search`)
- Create new tasks assigned to themselves (`POST /api/tasks/user/{userId}`)

#### **Shared (Admin + User) Endpoints**
- Get a single task if the user owns it (`GET /api/tasks/{id}`)
- Get all tasks (`GET /api/tasks`) – Admin sees all, Users see their own only