# ERASM (Enterprise Resource Allocation & Skill Management System) Documentation

## 1. Project Overview & Working

ERASM is a comprehensive, production-grade Spring Boot application designed to manage an organization's workforce, their skills, projects, and resource allocations. It solves the common problems of poor visibility into employee competencies, manual resource allocation, and untracked utilization.

### How It Works (Core Modules)

1.  **Authentication & Security**: Uses Spring Security with JWT. Users register and log in to receive an access token and a refresh token. Role-Based Access Control (RBAC) ensures users only access what they are permitted to (e.g., `ADMIN`, `DELIVERY_MANAGER`, `RESOURCE_MANAGER`, `EMPLOYEE`, `AUDITOR`).
2.  **User & Employee Management**: Users are the authenticated entities, while Employees hold the actual HR profiles (designation, department, bench status).
3.  **Skill Inventory**: A centralized repository of skills (e.g., Java, React, AWS). Employees can be tagged with these skills along with their proficiency levels.
4.  **Project Management**: Delivery managers create projects and define budgets and timelines.
5.  **Resource Requests (The Core Workflow)**:
    *   A Delivery Manager needs a resource for a project, so they create a `ResourceRequest` (DRAFT).
    *   They submit it (SUBMITTED).
    *   A Resource Manager reviews it (UNDER_REVIEW) and either Approves or Rejects it.
6.  **Allocations & Utilization**:
    *   Once a request is approved, the Resource Manager allocates an Employee to the project for a specific percentage (1-100%).
    *   The system strictly ensures an employee's total active allocation never exceeds 100%.
    *   When total allocation drops to 0%, the employee is marked as being on the "bench".

---

## 2. Postman Testing Guide & Snippets

Below are the Postman testing snippets (JSON bodies and expected configurations) for the complete core workflow of the application.

> **Base URL:** `http://localhost:8080`
> **Headers:** For all endpoints except `/auth/register` and `/auth/login`, you must include:
> `Authorization: Bearer <your_jwt_token>`

### 2.1 Authentication

#### Register a New User
*   **Method / URL:** `POST /auth/register`
*   **Request Body (JSON):**
    ```json
    {
      "email": "admin@erasm.com",
      "password": "Password@123!",
      "role": "ADMIN"
    }
    ```
*   **Expected Output (201 Created):**
    ```json
    {
      "success": true,
      "message": "User registered successfully",
      "data": {
        "id": 1,
        "email": "admin@erasm.com",
        "role": "ADMIN"
      }
    }
    ```

#### Login
*   **Method / URL:** `POST /auth/login`
*   **Request Body (JSON):**
    ```json
    {
      "email": "admin@erasm.com",
      "password": "Password@123!"
    }
    ```
*   **Expected Output (200 OK):**
    ```json
    {
      "success": true,
      "message": "Login successful",
      "data": {
        "accessToken": "eyJhbG...",
        "refreshToken": "eyJhbG...",
        "tokenType": "Bearer",
        "email": "admin@erasm.com",
        "role": "ADMIN"
      }
    }
    ```
    *(Copy the `accessToken` for subsequent requests)*

### 2.2 Skill Management (Requires ADMIN)

#### Add a New Skill
*   **Method / URL:** `POST /skills`
*   **Request Body (JSON):**
    ```json
    {
      "name": "Spring Boot",
      "category": "Backend",
      "description": "Java Spring Boot framework"
    }
    ```
*   **Expected Output (201 Created):**
    ```json
    {
      "success": true,
      "data": { "id": 1, "name": "Spring Boot", "category": "Backend" }
    }
    ```

### 2.3 Employee Profiles

#### Create Employee Profile (Requires ADMIN)
*   **Method / URL:** `POST /employees`
*   **Request Body (JSON):**
    ```json
    {
      "userId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "phone": "1234567890",
      "department": "Engineering",
      "designation": "Senior Developer",
      "dateOfJoining": "2024-01-01",
      "employmentType": "FULL_TIME",
      "totalExperienceYears": 5.0
    }
    ```
*   **Expected Output (201 Created):**
    ```json
    {
      "success": true,
      "data": { "id": 1, "firstName": "John", "benchStatus": true }
    }
    ```

#### Add Skill to Employee
*   **Method / URL:** `POST /employees/1/skills`
*   **Request Body (JSON):**
    ```json
    {
      "skillId": 1,
      "level": "ADVANCED",
      "yearsOfExperience": 4.5
    }
    ```

### 2.4 Project & Resource Requests

#### Create a Project (Requires DELIVERY_MANAGER or ADMIN)
*   **Method / URL:** `POST /projects`
*   **Request Body (JSON):**
    ```json
    {
      "name": "Acme Corp Migration",
      "clientName": "Acme Corp",
      "description": "Cloud migration project",
      "startDate": "2025-01-01",
      "endDate": "2025-12-31",
      "technologyStack": "Java, Spring Boot, AWS",
      "budget": 500000.00
    }
    ```
*   **Expected Output (201 Created):**
    ```json
    {
      "success": true,
      "data": { "id": 1, "name": "Acme Corp Migration", "status": "ACTIVE" }
    }
    ```

#### Create Resource Request (Requires DELIVERY_MANAGER)
*   **Method / URL:** `POST /resource-requests`
*   **Request Body (JSON):**
    ```json
    {
      "projectId": 1,
      "skillId": 1,
      "skillLevel": "ADVANCED",
      "requiredCount": 1,
      "requestedStartDate": "2025-01-01",
      "requestedEndDate": "2025-12-31",
      "remarks": "Need an urgent backend developer"
    }
    ```
*   **Expected Output (201 Created):** Status will be `DRAFT`.

#### Submit Request (Requires DELIVERY_MANAGER)
*   **Method / URL:** `PUT /resource-requests/1/submit`
*   **Expected Output (200 OK):** Status changes to `SUBMITTED`.

#### Approve Request (Requires RESOURCE_MANAGER)
*   **Method / URL:** `PUT /resource-requests/1/approve`
*   **Expected Output (200 OK):** Status changes to `APPROVED`.

### 2.5 Allocation (Requires RESOURCE_MANAGER)

#### Allocate Employee to Project
*   **Method / URL:** `POST /allocations`
*   **Request Body (JSON):**
    ```json
    {
      "employeeId": 1,
      "projectId": 1,
      "resourceRequestId": 1,
      "allocationPercentage": 100,
      "startDate": "2025-01-01",
      "endDate": "2025-12-31"
    }
    ```
*   **Expected Output (201 Created):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
        "employeeId": 1,
        "projectId": 1,
        "allocationPercentage": 100,
        "status": "ACTIVE"
      }
    }
    ```

### 2.6 Dashboard & Reports

#### View Utilization Report
*   **Method / URL:** `GET /reports/utilization`
*   **Expected Output (200 OK):** A summary of all employees' utilized percentages vs bench percentages.

#### View Audit Logs (Requires ADMIN or AUDITOR)
*   **Method / URL:** `GET /audit-logs`
*   **Expected Output (200 OK):** A list of all actions performed in the system (e.g., login events, allocation creations).
