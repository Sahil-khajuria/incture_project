# ERASM - Enterprise Resource Allocation & Skill Management System

## Tech Stack
Java 17+ | Spring Boot 3.2.5 | MySQL 8.4.9 | Spring Security + JWT | Lombok | JaCoCo | Springdoc OpenAPI

## Setup and Run Instructions

### Prerequisites
- **Java**: JDK 21
- **Maven**: 3.8+
- **MySQL**: 8.4+

### 1. Database Setup
Log in to MySQL and create the database:
```bash
mysql -u root -p -e "CREATE DATABASE erasm_db;"
```
Initialize the schema (make sure you are in the project root directory):
```bash
mysql -u root -p erasm_db < src/main/resources/db/schema.sql
```

### 2. Environment Variables
The application relies on the following environment variables. Set them in your terminal before running:
```bash
export DB_USERNAME=root
export DB_PASSWORD=root
export JWT_SECRET=your-256-bit-secret-key-that-is-very-long
export JWT_ACCESS_EXPIRY=900000
export JWT_REFRESH_EXPIRY=604800000
```

### 3. Build and Run
To run the test suite and build the project:
```bash
mvn clean install
```
Run the Spring Boot application:
```bash
mvn spring-boot:run
```
*(If you have multiple Java versions installed, ensure JAVA_HOME points to Java 21: `JAVA_HOME=/usr/lib/jvm/java-21-openjdk mvn spring-boot:run`)*

### 4. Access the Application
- **API Base URL**: `http://localhost:8080`
- **Swagger UI / API Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Default Admin: `admin@erasm.com` / `Admin@1234`

## Roles: ADMIN, DELIVERY_MANAGER, RESOURCE_MANAGER, EMPLOYEE, AUDITOR

## Key Business Rules
1. **100% Allocation Cap** — Employee allocations cannot exceed 100% total
2. **Request Workflow** — DRAFT → SUBMITTED → UNDER_REVIEW → APPROVED/REJECTED → ALLOCATED → COMPLETED
3. **Account Locking** — 5 failed login attempts = 30-minute lockout
4. **Soft Deletes** — No hard deletes; entities are deactivated
5. **Project Close** — Releases all active allocations and recalculates bench
