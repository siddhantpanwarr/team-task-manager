# Team Task Manager Web Application

## 1) High-Level Architecture Explanation

The solution is a two-tier web application with a REST API backend and SPA frontend:

- **Frontend (React + Axios):** Handles login/register, role-aware UI, dashboard cards, projects, and tasks.
- **Backend (Spring Boot):** Implements authentication, authorization, business rules, and persistence.
- **Database (MySQL):** Stores users, projects, project_members, and tasks.
- **Security:** JWT-based stateless auth with BCrypt password hashing and role-based access.

Flow:
1. User logs in or registers (`/api/auth/*`), backend returns JWT.
2. Frontend stores JWT in `localStorage`.
3. Axios sends `Authorization: Bearer <token>` for protected requests.
4. Spring Security filter validates JWT and sets authenticated principal.
5. Services enforce admin/member business permissions.

---

## 2) Database Schema (Tables + Relationships)

### ER Diagram (Text)

See `database/er-diagram.txt`.

### SQL Schema

See `database/schema.sql`.

### Relationships

- `User <-> Project`: Many-to-Many through `project_members`
- `Project -> Task`: One-to-Many
- `User -> Task`: One-to-Many (`assigned_user_id`)
- `Project.created_by_id -> User.id`: Many-to-One (project creator admin)

---

## 3) Backend Implementation (Spring Boot)

### Project Structure

```text
backend/
  src/main/java/com/example/teamtaskmanager/
    config/
    controller/
    dto/
    entity/
    exception/
    repository/
    security/
    service/
      impl/
```

### Entities

- `User`: id, fullName, email, password (BCrypt), role, createdAt
- `Project`: id, name, description, createdBy, teamMembers
- `Task`: id, title, description, status, dueDate, assignedUser, project
- Enums: `Role`, `TaskStatus`

### Repositories

- `UserRepository`
- `ProjectRepository`
- `TaskRepository`

### Services

- `AuthService` + `AuthServiceImpl`
- `ProjectService` + `ProjectServiceImpl`
- `TaskService` + `TaskServiceImpl`
- `DashboardService` + `DashboardServiceImpl`

### Controllers

- `AuthController`: register/login
- `ProjectController`: create/get/update/delete projects
- `TaskController`: create/get my tasks/get by project/update status
- `DashboardController`: member dashboard metrics
- `UserController`: user listing for member assignment UI

### Security Config (JWT)

- `JwtService`: token generation + validation
- `JwtAuthenticationFilter`: parses bearer token and authenticates requests
- `CustomUserDetailsService`: loads users from DB
- `SecurityConfig`: stateless security chain + route protection
- `CorsConfig`: configurable allowed origins

### Validation + Exceptions

- Validation via Jakarta annotations in DTOs
- Global exception handler in `ApiExceptionHandler`

---

## 4) Frontend Implementation (React)

### Folder Structure

```text
frontend/
  src/
    api/
      client.js
    components/
      AppLayout.jsx
      TaskStatusBadge.jsx
    context/
      AuthContext.jsx
    pages/
      LoginPage.jsx
      RegisterPage.jsx
      DashboardPage.jsx
      ProjectsPage.jsx
      MyTasksPage.jsx
    routes/
      ProtectedRoute.jsx
    styles/
      app.css
    App.jsx
    main.jsx
```

### Components and Pages

- **Auth Pages:** login/register forms with backend API calls
- **Dashboard:** total/completed/pending/overdue tasks
- **Projects:** admin can create projects and assign members
- **My Tasks:** member/admin can see own tasks and update status
- **ProtectedRoute:** blocks unauthenticated routes

### API Integration

- Axios instance in `src/api/client.js`
- JWT auto-attached via request interceptor
- Base URL via `VITE_API_BASE_URL`

---

## 5) Step-by-Step Run Instructions (Local Setup)

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+ and npm
- MySQL 8+

## Backend

1. Create DB:
   - `CREATE DATABASE team_task_manager;`
2. Go to backend:
   - `cd backend`
3. Set env vars:
   - `DB_URL=jdbc:mysql://localhost:3306/team_task_manager?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
   - `DB_USERNAME=root`
   - `DB_PASSWORD=your_password`
   - `JWT_SECRET=your-super-long-random-secret-at-least-32-chars`
   - `ALLOWED_ORIGINS=http://localhost:5173`
4. Run:
   - `mvn spring-boot:run`

## Frontend

1. Go to frontend:
   - `cd frontend`
2. Install deps:
   - `npm install`
3. Set env vars (`.env`):
   - `VITE_API_BASE_URL=http://localhost:8080/api`
4. Run:
   - `npm run dev`

---

## 6) Deployment Steps (Railway + Vercel/Netlify)

## Deploy Backend + MySQL on Railway

1. Push code to GitHub.
2. Create Railway project.
3. Add **MySQL service** in Railway.
4. Add **Backend service** from repo and set root directory to `backend`.
5. Configure backend env vars:
   - `DB_URL` = Railway MySQL JDBC URL
   - `DB_USERNAME` = Railway MySQL username
   - `DB_PASSWORD` = Railway MySQL password
   - `JWT_SECRET` = secure random string (>=32 chars)
   - `JWT_EXPIRATION_MS` = `86400000`
   - `ALLOWED_ORIGINS` = your frontend URL (Vercel/Netlify domain)
6. Build command:
   - `mvn clean package`
7. Start command:
   - `java -jar target/team-task-manager-0.0.1-SNAPSHOT.jar`
8. Deploy and copy Railway backend public URL.

## Deploy Frontend on Vercel

1. Create Vercel project from same repo.
2. Set root directory to `frontend`.
3. Set env var:
   - `VITE_API_BASE_URL=https://<your-railway-backend-domain>/api`
4. Build command:
   - `npm run build`
5. Output directory:
   - `dist`
6. Deploy.

## Netlify Alternative

- Same frontend root (`frontend`)
- Build command: `npm run build`
- Publish directory: `dist`
- Set `VITE_API_BASE_URL` in Netlify environment settings

---

## 7) README.md Content

This file itself (`README.md`) is the final project README content and can be used directly.

---

## 8) Suggestions for Demo Video

1. **Intro (30 sec):** project goals and tech stack.
2. **Auth demo:** register admin, register member, login flow.
3. **Admin flow:** create project, assign team member, create tasks.
4. **Member flow:** login as member, view assigned tasks, update status.
5. **Dashboard:** show metrics update after status changes.
6. **API security:** show unauthorized request rejection without JWT.
7. **Deployment:** show live Railway + Vercel app URLs.
8. **Wrap-up:** architecture and future enhancements.

---

## Environment Variable Reference

### Backend

- `PORT` (optional)
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS` (optional)
- `ALLOWED_ORIGINS`

### Frontend

- `VITE_API_BASE_URL`
