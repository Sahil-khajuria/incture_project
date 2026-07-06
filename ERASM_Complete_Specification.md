# Enterprise Resource Allocation & Skill Management System (ERASM)
## Complete Agent Build Prompt

---

## 1. PROJECT OVERVIEW

You are tasked with building a **production-grade, full-stack Java backend application** called the **Enterprise Resource Allocation & Skill Management System (ERASM)**. This is a centralized platform that helps organizations manage employees, skills, projects, and resource allocation efficiently.

**Problem being solved:**
- Employees remain on the bench without allocation
- Difficulty identifying employees with required skills
- Lack of visibility into employee competencies
- Manual resource allocation processes
- No centralized skill inventory
- Lack of utilization tracking
- No approval workflow for resource allocation

---

## 2. TECHNOLOGY STACK

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| ORM | Hibernate / Spring Data JPA |
| Database | MySQL 8.x (or PostgreSQL) |
| Security | Spring Security 6.x + JWT (jjwt 0.11+) |
| Password Hashing | BCrypt |
| Testing | JUnit 5 + Mockito |
| Logging | SLF4J + Logback |
| Build Tool | Maven |
| Version Control | Git (GitHub) |
| API Documentation | Springdoc OpenAPI (Swagger UI) |

---

## 3. PROJECT STRUCTURE

Create the project following this exact layered architecture:

```
erasm/
├── src/
│   ├── main/
│   │   ├── java/com/erasm/
│   │   │   ├── ErasmApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── EmployeeController.java
│   │   │   │   ├── SkillController.java
│   │   │   │   ├── ProjectController.java
│   │   │   │   ├── ResourceRequestController.java
│   │   │   │   ├── AllocationController.java
│   │   │   │   ├── ReportController.java
│   │   │   │   └── AuditController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── EmployeeService.java
│   │   │   │   ├── SkillService.java
│   │   │   │   ├── ProjectService.java
│   │   │   │   ├── ResourceRequestService.java
│   │   │   │   ├── AllocationService.java
│   │   │   │   ├── ReportService.java
│   │   │   │   └── AuditService.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── SkillRepository.java
│   │   │   │   ├── EmployeeSkillRepository.java
│   │   │   │   ├── CertificationRepository.java
│   │   │   │   ├── ProjectRepository.java
│   │   │   │   ├── ResourceRequestRepository.java
│   │   │   │   ├── AllocationRepository.java
│   │   │   │   └── AuditLogRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Employee.java
│   │   │   │   ├── Skill.java
│   │   │   │   ├── EmployeeSkill.java
│   │   │   │   ├── Certification.java
│   │   │   │   ├── Project.java
│   │   │   │   ├── ResourceRequest.java
│   │   │   │   ├── Allocation.java
│   │   │   │   └── AuditLog.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── ChangePasswordRequest.java
│   │   │   │   │   ├── EmployeeRequest.java
│   │   │   │   │   ├── SkillRequest.java
│   │   │   │   │   ├── EmployeeSkillRequest.java
│   │   │   │   │   ├── CertificationRequest.java
│   │   │   │   │   ├── ProjectRequest.java
│   │   │   │   │   ├── ResourceRequestDto.java
│   │   │   │   │   └── AllocationRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── EmployeeResponse.java
│   │   │   │       ├── SkillResponse.java
│   │   │   │       ├── ProjectResponse.java
│   │   │   │       ├── AllocationResponse.java
│   │   │   │       ├── UtilizationResponse.java
│   │   │   │       ├── ReportResponse.java
│   │   │   │       └── ApiResponse.java
│   │   │   ├── mapper/
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── EmployeeMapper.java
│   │   │   │   ├── SkillMapper.java
│   │   │   │   ├── ProjectMapper.java
│   │   │   │   └── AllocationMapper.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── UserNotFoundException.java
│   │   │   │   ├── ProjectNotFoundException.java
│   │   │   │   ├── SkillNotFoundException.java
│   │   │   │   ├── AllocationException.java
│   │   │   │   ├── ResourceRequestNotFoundException.java
│   │   │   │   ├── EmployeeNotFoundException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   └── UnauthorizedAccessException.java
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── enums/
│   │   │   │   ├── RoleName.java
│   │   │   │   ├── SkillLevel.java
│   │   │   │   ├── ProjectStatus.java
│   │   │   │   ├── RequestStatus.java
│   │   │   │   └── AllocationStatus.java
│   │   │   └── util/
│   │   │       ├── AuditUtil.java
│   │   │       └── ValidationUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── logback-spring.xml
│   │       └── db/
│   │           └── schema.sql
│   └── test/
│       └── java/com/erasm/
│           ├── service/
│           │   ├── AuthServiceTest.java
│           │   ├── EmployeeServiceTest.java
│           │   ├── SkillServiceTest.java
│           │   ├── ProjectServiceTest.java
│           │   ├── AllocationServiceTest.java
│           │   └── ResourceRequestServiceTest.java
│           └── controller/
│               ├── AuthControllerTest.java
│               └── EmployeeControllerTest.java
├── pom.xml
└── README.md
```

---

## 4. DATABASE DESIGN

### 4.1 All Required Tables

#### `roles`
```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE  -- ADMIN, DELIVERY_MANAGER, RESOURCE_MANAGER, EMPLOYEE, AUDITOR
);
```

#### `users`
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(150),
    modified_by VARCHAR(150),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

#### `employees`
```sql
CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(100),
    designation VARCHAR(100),
    date_of_joining DATE,
    employment_type VARCHAR(50),   -- FULL_TIME, PART_TIME, CONTRACT
    bench_status BOOLEAN DEFAULT TRUE,
    total_experience_years DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(150),
    modified_by VARCHAR(150),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `skills`
```sql
CREATE TABLE skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `employee_skills`
```sql
CREATE TABLE employee_skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    level VARCHAR(20) NOT NULL,    -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    years_of_experience DOUBLE DEFAULT 0.0,
    last_used_date DATE,
    UNIQUE KEY uq_emp_skill (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);
```

#### `certifications`
```sql
CREATE TABLE certifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    issuing_organization VARCHAR(200),
    issue_date DATE,
    expiry_date DATE,
    credential_id VARCHAR(200),
    credential_url VARCHAR(500),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
```

#### `projects`
```sql
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    client_name VARCHAR(200),
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',   -- ACTIVE, ON_HOLD, COMPLETED, CANCELLED
    technology_stack TEXT,                 -- comma-separated or JSON
    budget DECIMAL(15, 2),
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modified_by VARCHAR(150),
    FOREIGN KEY (created_by) REFERENCES users(id)
);
```

#### `resource_requests`
```sql
CREATE TABLE resource_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    requested_by BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    skill_level VARCHAR(20),
    required_count INT NOT NULL DEFAULT 1,
    status VARCHAR(50) DEFAULT 'DRAFT',    -- DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, ALLOCATED, COMPLETED
    remarks TEXT,
    requested_start_date DATE,
    requested_end_date DATE,
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (requested_by) REFERENCES users(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id),
    FOREIGN KEY (reviewed_by) REFERENCES users(id)
);
```

#### `allocations`
```sql
CREATE TABLE allocations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    resource_request_id BIGINT,
    allocation_percentage INT NOT NULL,    -- 1–100; total per employee across active allocations must not exceed 100
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',   -- ACTIVE, RELEASED, COMPLETED
    allocated_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modified_by VARCHAR(150),
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (resource_request_id) REFERENCES resource_requests(id),
    FOREIGN KEY (allocated_by) REFERENCES users(id)
);
```

#### `audit_logs`
```sql
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    action VARCHAR(50) NOT NULL,           -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT, APPROVE, REJECT
    performed_by VARCHAR(150),
    description TEXT,
    ip_address VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4.2 Seed Data
```sql
INSERT INTO roles (name) VALUES ('ADMIN'), ('DELIVERY_MANAGER'), ('RESOURCE_MANAGER'), ('EMPLOYEE'), ('AUDITOR');

INSERT INTO skills (name, category) VALUES
('Java', 'Backend'), ('Spring Boot', 'Backend'), ('React', 'Frontend'),
('Angular', 'Frontend'), ('AWS', 'Cloud'), ('Azure', 'Cloud'),
('MySQL', 'Database'), ('PostgreSQL', 'Database'), ('Docker', 'DevOps'), ('Kubernetes', 'DevOps');

-- Default admin user (password: Admin@1234 BCrypt-hashed)
INSERT INTO users (email, password, role_id)
VALUES ('admin@erasm.com', '$2a$10$...bcrypt_hash_here...', 1);
```

---

## 5. JPA ENTITY RELATIONSHIPS

Implement these exact JPA relationships:

| Relationship | Entities |
|---|---|
| `@OneToOne` | `User` ↔ `Employee` |
| `@OneToMany` | `Project` → `ResourceRequest` |
| `@ManyToOne` | `Employee` → `Role` (via User) |
| `@ManyToMany` | `Employee` ↔ `Skill` (via `EmployeeSkill` join table) |

All entities must extend or include audit fields: `createdAt`, `updatedAt`, `createdBy`, `modifiedBy`.

Use `@EntityListeners(AuditingEntityListener.class)` with Spring Data JPA Auditing enabled via `@EnableJpaAuditing` on the main class.

---

## 6. USER ROLES & PERMISSIONS

| Role | Key Permissions |
|---|---|
| `ADMIN` | Full access: manage users, roles, skills; view all reports and audit logs |
| `DELIVERY_MANAGER` | Create/update/close projects; raise resource requests; monitor allocation |
| `RESOURCE_MANAGER` | Allocate/reallocate/release employees; approve or reject resource requests |
| `EMPLOYEE` | Update own profile; add/update own skills and certifications; view own assignments |
| `AUDITOR` | Read-only: view audit logs, generate reports, track all activities |

---

## 7. COMPLETE API SPECIFICATION

### 7.1 Authentication APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/auth/register` | PUBLIC | Register a new user |
| POST | `/auth/login` | PUBLIC | Login and receive JWT |
| POST | `/auth/logout` | AUTHENTICATED | Invalidate token (server-side blacklist) |
| POST | `/auth/change-password` | AUTHENTICATED | Change own password |
| POST | `/auth/refresh-token` | PUBLIC | Refresh JWT using refresh token |

### 7.2 User Management APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/users` | ADMIN | List all users (paginated) |
| GET | `/users/{id}` | ADMIN | Get user by ID |
| PUT | `/users/{id}` | ADMIN | Update user details |
| DELETE | `/users/{id}` | ADMIN | Soft-delete user |
| PUT | `/users/{id}/assign-role` | ADMIN | Assign role to user |
| PUT | `/users/{id}/activate` | ADMIN | Activate user account |
| PUT | `/users/{id}/deactivate` | ADMIN | Deactivate user account |

### 7.3 Employee APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/employees` | ADMIN, RESOURCE_MANAGER | List all employees (paginated, filterable) |
| GET | `/employees/{id}` | ADMIN, RESOURCE_MANAGER, EMPLOYEE(own) | Get employee by ID |
| POST | `/employees` | ADMIN | Create employee profile |
| PUT | `/employees/{id}` | ADMIN, EMPLOYEE(own) | Update employee profile |
| DELETE | `/employees/{id}` | ADMIN | Soft-delete employee |
| GET | `/employees/bench` | RESOURCE_MANAGER, ADMIN | Get all bench employees |
| GET | `/employees/search` | RESOURCE_MANAGER, ADMIN | Search by skill, level, availability |

### 7.4 Skill Management APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/skills` | ALL ROLES | List all skills |
| GET | `/skills/{id}` | ALL ROLES | Get skill by ID |
| POST | `/skills` | ADMIN | Add new skill |
| PUT | `/skills/{id}` | ADMIN | Update skill |
| DELETE | `/skills/{id}` | ADMIN | Soft-delete skill |

### 7.5 Employee Skill Profile APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/employees/{id}/skills` | ALL ROLES | Get employee's skill list |
| POST | `/employees/{id}/skills` | EMPLOYEE(own), ADMIN | Add skill to employee |
| PUT | `/employees/{id}/skills/{skillId}` | EMPLOYEE(own), ADMIN | Update skill level/experience |
| DELETE | `/employees/{id}/skills/{skillId}` | EMPLOYEE(own), ADMIN | Remove skill from employee |
| GET | `/employees/{id}/certifications` | ALL ROLES | Get employee certifications |
| POST | `/employees/{id}/certifications` | EMPLOYEE(own), ADMIN | Add certification |
| PUT | `/employees/{id}/certifications/{certId}` | EMPLOYEE(own), ADMIN | Update certification |
| DELETE | `/employees/{id}/certifications/{certId}` | EMPLOYEE(own), ADMIN | Delete certification |

### 7.6 Project Management APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/projects` | ALL ROLES | List all projects (paginated) |
| GET | `/projects/{id}` | ALL ROLES | Get project by ID |
| POST | `/projects` | DELIVERY_MANAGER, ADMIN | Create project |
| PUT | `/projects/{id}` | DELIVERY_MANAGER, ADMIN | Update project |
| DELETE | `/projects/{id}` | ADMIN | Soft-delete project |
| PUT | `/projects/{id}/close` | DELIVERY_MANAGER, ADMIN | Close a project |
| GET | `/projects/{id}/allocations` | DELIVERY_MANAGER, RESOURCE_MANAGER, ADMIN | View project allocations |

### 7.7 Resource Request APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/resource-requests` | DELIVERY_MANAGER, RESOURCE_MANAGER, ADMIN | List all requests (filterable by status) |
| GET | `/resource-requests/{id}` | DELIVERY_MANAGER, RESOURCE_MANAGER, ADMIN | Get request by ID |
| POST | `/resource-requests` | DELIVERY_MANAGER | Create resource request (status = DRAFT) |
| PUT | `/resource-requests/{id}` | DELIVERY_MANAGER | Update a DRAFT request |
| DELETE | `/resource-requests/{id}` | DELIVERY_MANAGER | Delete DRAFT request only |
| PUT | `/resource-requests/{id}/submit` | DELIVERY_MANAGER | Submit request for review (DRAFT → SUBMITTED) |
| PUT | `/resource-requests/{id}/approve` | RESOURCE_MANAGER | Approve request (→ APPROVED) |
| PUT | `/resource-requests/{id}/reject` | RESOURCE_MANAGER | Reject request with reason (→ REJECTED) |

### 7.8 Allocation APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/allocations` | RESOURCE_MANAGER, ADMIN | List all allocations (paginated, filterable) |
| GET | `/allocations/{id}` | RESOURCE_MANAGER, ADMIN | Get allocation by ID |
| POST | `/allocations` | RESOURCE_MANAGER | Allocate employee to project |
| PUT | `/allocations/{id}` | RESOURCE_MANAGER | Reallocate (update percentage/dates) |
| PUT | `/allocations/{id}/release` | RESOURCE_MANAGER | Release employee from project |
| GET | `/employees/{id}/allocations` | ALL ROLES | View employee's current allocations |

### 7.9 Utilization Dashboard APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/dashboard/utilization` | RESOURCE_MANAGER, ADMIN, DELIVERY_MANAGER | All employees utilization summary |
| GET | `/dashboard/utilization/{employeeId}` | ALL ROLES | Single employee utilization |
| GET | `/dashboard/bench-summary` | RESOURCE_MANAGER, ADMIN | Count and list of benched employees |

### 7.10 Report APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/reports/skills` | ADMIN, AUDITOR | Employees grouped by skill |
| GET | `/reports/utilization` | ADMIN, AUDITOR, DELIVERY_MANAGER | Employee utilization percentages |
| GET | `/reports/project-allocation` | ADMIN, AUDITOR, DELIVERY_MANAGER | Employees assigned per project |

### 7.11 Audit Log APIs
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/audit-logs` | ADMIN, AUDITOR | View all audit logs (paginated, filterable by entity/action/date) |
| GET | `/audit-logs/{id}` | ADMIN, AUDITOR | Get single audit log entry |

---

## 8. BUSINESS LOGIC & VALIDATION RULES

### 8.1 User Registration
- Email must be unique (case-insensitive)
- Password must be minimum 8 characters, contain at least one uppercase, one lowercase, one digit, one special character
- Role must be provided and must be a valid role
- Email format must be valid
- On registration, an Employee profile is NOT auto-created; Admin must create it separately

### 8.2 Login
- Return JWT access token (expiry: 15 minutes) and refresh token (expiry: 7 days)
- Log login event to audit_logs
- Increment failed attempt counter; lock account after 5 consecutive failed logins
- Do NOT log password or token in any log

### 8.3 Allocation Rules (CRITICAL)
- An employee's total active allocation percentage across all projects must NEVER exceed 100%
- Validation logic:
  ```
  currentTotalAllocation = SUM of allocation_percentage WHERE employee_id = X AND status = 'ACTIVE'
  if (currentTotalAllocation + newAllocationPercentage) > 100 → throw AllocationException
  ```
- allocation_percentage must be between 1 and 100
- start_date must not be after end_date
- Employee must exist and be active
- Project must exist and be in ACTIVE status
- On allocation, if employee's total allocation reaches > 0%, set bench_status = false
- On release, recalculate; if total = 0%, set bench_status = true

### 8.4 Resource Request Workflow (State Machine)
Valid transitions ONLY:
```
DRAFT       → SUBMITTED      (by DELIVERY_MANAGER, via /submit)
SUBMITTED   → UNDER_REVIEW   (automatic on RESOURCE_MANAGER opening the request)
UNDER_REVIEW → APPROVED      (by RESOURCE_MANAGER)
UNDER_REVIEW → REJECTED      (by RESOURCE_MANAGER, requires remarks)
APPROVED    → ALLOCATED      (automatic when allocation is created against this request)
ALLOCATED   → COMPLETED      (when project closes or allocation ends)
REJECTED    → DRAFT          (DELIVERY_MANAGER can revise and resubmit)
```
Any other status transition must throw `AllocationException` with a clear message.

### 8.5 Utilization Calculation
```java
// Billable % = (Billable Hours / Total Hours) × 100
// Bench % = (Bench Hours / Total Hours) × 100

// For simplification, derive from allocation_percentage:
// If employee is allocated to any project → allocation_percentage is billable
// If total allocation < 100% → remaining % is bench time
// Bench % = 100 - SUM(active allocation_percentage for that employee)
```

### 8.6 Skill Level Enum
Only allowed values: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`, `EXPERT`

### 8.7 Project Status Rules
- A project with ACTIVE allocations cannot be DELETED (only soft-delete or CLOSE)
- Closing a project must set all active allocations for that project to COMPLETED and release the employees

### 8.8 Employee Skill Uniqueness
- An employee cannot add the same skill twice (unique constraint on employee_id + skill_id)
- Updating an existing skill should update the level and experience, not create a duplicate

### 8.9 Certification Expiry
- Issue date must not be in the future
- Expiry date must be after issue date
- Return a warning flag `isExpired: true` in response if expiry_date < today

### 8.10 Password Change
- Old password must be verified before allowing change
- New password cannot be the same as old password
- New password must meet the same complexity rules as registration

---

## 9. SECURITY IMPLEMENTATION

### 9.1 JWT Configuration
```properties
jwt.secret=your-256-bit-secret-key-stored-in-env-not-hardcoded
jwt.access-token-expiry=900000        # 15 minutes in ms
jwt.refresh-token-expiry=604800000    # 7 days in ms
```

### 9.2 JWT Token Provider
Implement `JwtTokenProvider` with:
- `generateAccessToken(UserDetails userDetails)` — includes email + role in claims
- `generateRefreshToken(String email)`
- `validateToken(String token)` — returns boolean, catches all JWT exceptions
- `getEmailFromToken(String token)`
- `getRoleFromToken(String token)`
- Do NOT include password or sensitive PII in token payload

### 9.3 JWT Authentication Filter
```java
// JwtAuthenticationFilter extends OncePerRequestFilter
// 1. Extract token from Authorization header (Bearer scheme)
// 2. Validate token
// 3. Load UserDetails from CustomUserDetailsService
// 4. Set SecurityContextHolder authentication
// 5. Log WARN on invalid token
```

### 9.4 Security Config
```java
// Permit these endpoints without auth:
// POST /auth/register
// POST /auth/login
// POST /auth/refresh-token
// GET /swagger-ui/** and /v3/api-docs/**

// All other endpoints require authentication
// Use @PreAuthorize at method level for role-based access
```

### 9.5 RBAC Annotations
Use on service or controller methods:
```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('RESOURCE_MANAGER')")
@PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_MANAGER')")
@PreAuthorize("hasRole('EMPLOYEE') and #id == authentication.principal.id")
```

### 9.6 Password Encryption
- Always use `BCryptPasswordEncoder` with strength 10
- Never store or return plain-text passwords
- Never log passwords

---

## 10. EXCEPTION HANDLING

### 10.1 Custom Exceptions
```java
// All extend RuntimeException with message constructor

public class UserNotFoundException extends RuntimeException { ... }
public class EmployeeNotFoundException extends RuntimeException { ... }
public class ProjectNotFoundException extends RuntimeException { ... }
public class SkillNotFoundException extends RuntimeException { ... }
public class ResourceRequestNotFoundException extends RuntimeException { ... }
public class AllocationException extends RuntimeException { ... }
public class DuplicateResourceException extends RuntimeException { ... }
public class UnauthorizedAccessException extends RuntimeException { ... }
public class InvalidStatusTransitionException extends RuntimeException { ... }
public class AccountLockedException extends RuntimeException { ... }
```

### 10.2 Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // UserNotFoundException → 404 NOT FOUND
    // EmployeeNotFoundException → 404 NOT FOUND
    // ProjectNotFoundException → 404 NOT FOUND
    // SkillNotFoundException → 404 NOT FOUND
    // ResourceRequestNotFoundException → 404 NOT FOUND

    // AllocationException → 400 BAD REQUEST
    // DuplicateResourceException → 409 CONFLICT
    // InvalidStatusTransitionException → 400 BAD REQUEST
    // AccountLockedException → 423 LOCKED

    // UnauthorizedAccessException → 403 FORBIDDEN
    // AccessDeniedException → 403 FORBIDDEN
    // AuthenticationException → 401 UNAUTHORIZED

    // MethodArgumentNotValidException → 400 BAD REQUEST (return field-level error map)
    // ConstraintViolationException → 400 BAD REQUEST

    // Exception (catch-all) → 500 INTERNAL SERVER ERROR (do NOT expose stack trace)
}
```

### 10.3 Standard Error Response DTO
```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String errorCode;    // e.g., "USER_NOT_FOUND", "ALLOCATION_EXCEEDED"
    // Include `errors: Map<String, String>` for validation errors
}
```

All API responses (success and error) must use this wrapper.

---

## 11. LOGGING REQUIREMENTS

### 11.1 Configuration (`logback-spring.xml`)
- Log to console AND rolling file (`logs/erasm.log`)
- Rolling policy: daily + max file size 10MB, keep 30 days
- Log format: `[TIMESTAMP] [LEVEL] [THREAD] [CLASS] - MESSAGE`

### 11.2 What to Log

**INFO level:**
- User login: `"User [email] logged in successfully"`
- User logout: `"User [email] logged out"`
- Project created: `"Project [name] created by [user]"`
- Resource request submitted: `"Request [id] submitted for project [projectId]"`
- Resource allocated: `"Employee [id] allocated to project [id] at [X]%"`
- Status transitions: `"Request [id] status changed from [OLD] to [NEW] by [user]"`

**WARN level:**
- Invalid login attempt: `"Failed login attempt for email [email]"`
- Unauthorized access attempt: `"Access denied for user [email] to [endpoint]"`
- Invalid request (validation failure): `"Validation failed for [entity]: [fields]"`
- Allocation overlap attempt: `"Allocation rejected for employee [id]: would exceed 100%"`
- Expired certification access: `"Certification [id] is expired"`

**ERROR level:**
- Database failure: `"Database error during [operation]: [exception class]"`
- System exception: `"Unexpected error in [class.method]: [exception message]"`
- JWT error: `"JWT validation failed: [reason]"` (do NOT log the token itself)

**NEVER log:**
- Passwords (plain or hashed)
- JWT tokens
- Personal information (phone number, address)
- Credit/financial details

---

## 12. TESTING REQUIREMENTS

### 12.1 Coverage Target
Minimum **80% line and branch coverage** across service layer. Use JaCoCo Maven plugin.

### 12.2 Service Layer Tests (JUnit 5 + Mockito)
Write tests for every public method in all service classes. Cover:

**AuthService:**
- Register with valid data → success
- Register with duplicate email → `DuplicateResourceException`
- Register with invalid password (too short, no special char) → validation error
- Login with correct credentials → returns JWT
- Login with wrong password → `AuthenticationException`
- Login with non-existent email → `UserNotFoundException`
- Change password with correct old password → success
- Change password with wrong old password → exception
- Change password to same as old → exception

**AllocationService:**
- Allocate employee at 60% → success
- Allocate same employee at additional 40% → total 100%, success
- Allocate same employee at additional 50% (total 110%) → `AllocationException`
- Allocate to non-existent project → `ProjectNotFoundException`
- Allocate non-existent employee → `EmployeeNotFoundException`
- Release employee → bench_status set to true if total = 0
- Reallocate → updates percentage, re-validates total

**EmployeeService:**
- Get all employees → returns paginated list
- Get employee by ID → success
- Get non-existent employee → `EmployeeNotFoundException`
- Update employee profile → success
- Get bench employees → returns only bench_status = true

**SkillService:**
- Add skill → success
- Add duplicate skill → `DuplicateResourceException`
- Update skill → success
- Delete non-existent skill → `SkillNotFoundException`
- Add skill to employee already having it → `DuplicateResourceException`

**ProjectService:**
- Create project → success
- Close project with active allocations → all allocations set to COMPLETED
- Delete project with active allocations → `AllocationException`
- Update non-existent project → `ProjectNotFoundException`

**ResourceRequestService:**
- Submit DRAFT request → status = SUBMITTED
- Submit already SUBMITTED request → `InvalidStatusTransitionException`
- Approve SUBMITTED request → `InvalidStatusTransitionException` (must be UNDER_REVIEW)
- Approve UNDER_REVIEW request → status = APPROVED
- Reject without remarks → validation error

### 12.3 Controller Layer Tests (MockMvc or WebMvcTest)
At minimum:
- `AuthController`: test `/auth/register` and `/auth/login` for 200, 400, 409, 401
- `EmployeeController`: test GET all (authenticated), GET by ID (not found = 404), unauthorized access = 403

---

## 13. CONFIGURATION FILES

### 13.1 `application.properties`
```properties
spring.application.name=erasm

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/erasm_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT (override via env variables in production)
jwt.secret=${JWT_SECRET:erasm-default-secret-change-in-production-256-bits}
jwt.access-token-expiry=900000
jwt.refresh-token-expiry=604800000

# Pagination defaults
spring.data.web.pageable.default-page-size=20
spring.data.web.pageable.max-page-size=100

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized

# Logging
logging.level.com.erasm=INFO
logging.level.org.springframework.security=WARN
logging.file.name=logs/erasm.log
```

### 13.2 `pom.xml` Key Dependencies
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- MySQL Driver -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- OpenAPI / Swagger -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<!-- JaCoCo Plugin for coverage -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
        <execution>
            <id>check</id>
            <goals><goal>check</goal></goals>
            <configuration>
                <rules>
                    <rule>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

## 14. GIT WORKFLOW

### 14.1 Branch Structure
```
main          ← production-ready only, protected
develop       ← integration branch for features
feature/*     ← individual feature branches (e.g., feature/user-management)
release/*     ← release stabilization (e.g., release/1.0.0)
hotfix/*      ← urgent production fixes
```

### 14.2 Commit Message Format (Conventional Commits)
```
feat: add JWT authentication
feat: implement resource allocation with 100% cap validation
fix: resolve null pointer in AllocationService
fix: correct JWT expiry calculation
refactor: extract allocation validation to separate method
test: add unit tests for AllocationService
chore: update dependencies in pom.xml
docs: add API documentation for project endpoints
```

### 14.3 Required Branches to Create
- `develop`
- `feature/user-management`
- `feature/employee-skill-profile`
- `feature/project-management`
- `feature/resource-request-workflow`
- `feature/resource-allocation`
- `feature/utilization-dashboard`
- `feature/audit-reporting`

Merge each feature branch into `develop` via Pull Request. Final merge: `develop` → `main`.

---

## 15. AUDIT LOGGING IMPLEMENTATION

Implement a reusable `AuditService` that logs every critical action:

```java
@Service
public class AuditService {
    public void log(String entityType, Long entityId, String action, String performedBy, String description) {
        // Persist to audit_logs table
    }
}
```

Call `auditService.log(...)` in every service method that performs CREATE, UPDATE, DELETE, LOGIN, LOGOUT, APPROVE, REJECT, ALLOCATE, RELEASE.

---

## 16. UTILIZATION DASHBOARD LOGIC

```java
// UtilizationResponse per employee:
public class UtilizationResponse {
    private Long employeeId;
    private String employeeName;
    private int totalAllocationPercentage;    // SUM of active allocations
    private int billablePercentage;           // same as totalAllocationPercentage
    private int benchPercentage;              // 100 - totalAllocationPercentage
    private boolean onBench;                  // benchPercentage == 100
    private List<ProjectAllocationSummary> projects;
}
```

---

## 17. REPORT SPECIFICATIONS

### Skill Report Response
```json
{
  "skill": "Java",
  "totalEmployees": 15,
  "byLevel": {
    "BEGINNER": 3,
    "INTERMEDIATE": 7,
    "ADVANCED": 4,
    "EXPERT": 1
  },
  "employees": [ { "id": 1, "name": "John Doe", "level": "ADVANCED" } ]
}
```

### Utilization Report Response
```json
{
  "totalEmployees": 50,
  "benchCount": 8,
  "fullyAllocatedCount": 30,
  "partiallyAllocatedCount": 12,
  "employees": [ { "id": 1, "name": "Jane", "billable": 80, "bench": 20 } ]
}
```

### Project Allocation Report Response
```json
{
  "project": "Healthcare Portal",
  "status": "ACTIVE",
  "allocatedEmployees": 5,
  "employees": [ { "id": 2, "name": "Bob", "allocationPercentage": 100 } ]
}
```

---

## 18. EDGE CASES TO HANDLE

1. **Concurrent allocation**: Two requests allocating the same employee simultaneously could both pass the 100% check. Use `@Transactional` with `SERIALIZABLE` isolation or pessimistic locking on allocation records for the employee.

2. **Deleted user's audit trail**: When a user is soft-deleted, their audit logs must be preserved. Never hard-delete audit_logs rows.

3. **Employee assigned to closed project**: When a project is closed, automatically release all active allocations and update bench status.

4. **Skill deleted still referenced**: Soft-delete skills only. Never hard-delete a skill that has employee_skill records referencing it.

5. **Role not found on register**: Return `400 Bad Request` with message "Invalid role specified", not a 500.

6. **JWT expired token**: Return `401 Unauthorized` with body `{ "success": false, "message": "Token has expired", "errorCode": "TOKEN_EXPIRED" }`.

7. **Empty pagination results**: Return `200 OK` with empty list and page metadata, never a 404.

8. **Allocation percentage = 0**: Reject with `400 BAD REQUEST` — allocation percentage must be at least 1%.

9. **Self-allocation attempt**: An employee cannot allocate themselves (same user_id as allocated_by). Validate in service layer.

10. **Date validation**: `start_date` for allocation/project must not be before today (allow today). `end_date` must be after `start_date`.

11. **Inactive user login**: Return `403 FORBIDDEN` with message "Account is deactivated. Contact admin."

12. **Resubmit after rejection**: A REJECTED request can go back to DRAFT, then re-submitted. Do not allow direct REJECTED → SUBMITTED transition.

13. **Pagination/sorting parameters**: Handle `page`, `size`, `sort` query params on all list endpoints. Return appropriate errors for invalid values (negative page, size > max).

14. **Case-insensitive email**: Store email in lowercase; compare case-insensitively.

15. **XSS / Input sanitization**: Use `@Valid` + Bean Validation on all request DTOs. Reject HTML/script tags in string fields.

---

## 19. DELIVERABLES CHECKLIST

Upon completion, ensure all of the following are present:

- [ ] GitHub repository with all branches as specified in Section 14
- [ ] Working Spring Boot application that starts without errors
- [ ] All 10 database tables created via `schema.sql`
- [ ] All APIs listed in Section 7 implemented and tested via Postman
- [ ] JWT authentication working end-to-end
- [ ] Role-based access control enforced on every endpoint
- [ ] Allocation 100% cap validation working
- [ ] Resource request workflow state machine enforced
- [ ] Global exception handler returning standard `ApiResponse` for all errors
- [ ] SLF4J logging configured for INFO/WARN/ERROR as specified
- [ ] JUnit 5 tests covering minimum 80% of service layer
- [ ] JaCoCo report generated and attached
- [ ] Swagger UI accessible at `/swagger-ui/index.html`
- [ ] `README.md` with: project description, setup steps, API list, environment variables required
- [ ] `schema.sql` with all DDL and seed data
- [ ] Postman collection exported as JSON

---

## 20. IMPLEMENTATION ORDER (RECOMMENDED)

Build in this sequence to minimize dependency issues:

1. **Project setup** — Spring Boot init, pom.xml, application.properties, logback config
2. **Database** — schema.sql, run migrations, verify tables
3. **Entities + Repositories** — all JPA entities with relationships, all Spring Data repositories
4. **Enums + DTOs** — all request/response DTOs with Bean Validation annotations
5. **Security** — JwtTokenProvider, JwtAuthenticationFilter, SecurityConfig, CustomUserDetailsService
6. **Auth module** — AuthService + AuthController (register, login, refresh, change-password)
7. **User Management** — UserService + UserController
8. **Skill Management** — SkillService + SkillController
9. **Employee module** — EmployeeService + EmployeeController (profile, skills, certifications)
10. **Project module** — ProjectService + ProjectController
11. **Resource Request module** — ResourceRequestService + ResourceRequestController (with workflow)
12. **Allocation module** — AllocationService + AllocationController (with 100% cap)
13. **Utilization Dashboard** — UtilizationService + DashboardController
14. **Reports + Audit** — ReportService + AuditService + controllers
15. **Exception Handling** — GlobalExceptionHandler (wire all exceptions)
16. **Mappers** — All entity ↔ DTO mappers
17. **Unit Tests** — Service tests with Mockito (target 80%+)
18. **Controller Tests** — MockMvc tests for auth and employee
19. **Integration testing** — End-to-end test via Postman/Swagger
20. **Documentation + Git cleanup** — README, Postman export, final branch merges

---

*This prompt is fully self-contained. No additional context is required. Build the complete ERASM application following all specifications above.*
