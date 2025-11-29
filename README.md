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
- Improved validation and error handling  

### Testing, Docker & Deployment
- Unit tests using **JUnit 5** and **Mockito**  
- Repository slicing tests with `@DataJpaTest`  
- Dockerfile for containerizing the application  
- `docker-compose` for app + database  
- GitHub Actions CI pipeline (build + tests)  
- Cloud deployment (Render / AWS Elastic Beanstalk)  

---

## Tech Stack

### **Backend**
- Java 17  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Hibernate ORM  
- Jakarta Validation (Bean Validation)  

### **Database**
- PostgreSQL  
- Flyway or schema auto-generation (`ddl-auto`)  

### **DevOps**
- Docker  
- Docker Compose  
- GitHub Actions (CI)  
- Render.com

### **Testing**
- JUnit 5  
- Mockito  
- MockMvc  

### **Documentation**
- Swagger / springdoc-openapi 
- https://task-manager-wc0c.onrender.com/swagger-ui/index.html
