# API Documentation

Base URL: `http://`

## Authentication

All requests (except `/auth/register` and `/auth/login`) require header:

```
Authorization: Bearer <access_token>
```

## Conventions

* API uses `camelCase`
* Database uses `snake_case`
* All dates are in ISO 8601 format

## Enums

**User Roles:**

```
USER, ADMIN
```

**Goal Status:**

```
ACTIVE, COMPLETED, OVERDUE
```

**Task Status:**

```
TODO, IN_PROGRESS, DONE, OVERDUE
```

## Status Logic

* Task becomes `OVERDUE` if `endDate < current date` and `status != DONE`
* Goal becomes `OVERDUE` if `endDate < current date` and not completed
* Status `OVERDUE` is assigned automatically by the system

## Scheduling Logic

Templates store relative timing:

* `daysOffset`
* `durationDays`

When a goal is created:

```
task.startDate = goal.startDate + daysOffset
task.endDate = task.startDate + durationDays
```

* Goal `endDate` is calculated as the latest task `endDate`
* Dates are fixed after goal creation (no automatic shifting)

---

## Endpoints

### POST /auth/register

Register a new user.

**Request:**

```json
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "secret123"
}
```

**Response 201:**

```json
{
  "id": 1,
  "email": "user@example.com",
  "role": "USER"
}
```

---

### POST /auth/login

User login.

**Request:**

```json
{
  "email": "user@example.com",
  "password": "secret123"
}
```

**Response 200:**

```json
{
  "accessToken": "eyJ...",
  "expiresIn": 3600
}
```

---

### POST /auth/logout

Logout user.

**Response 200:**

```json
{
  "message": "Logged out successfully"
}
```

---

### GET /templates

Get list of available templates.

**Response 200:**

```json
{
  "data": [
    {
      "id": 1,
      "title": "Become Backend Developer",
      "description": "Step-by-step roadmap"
    }
  ]
}
```

---

### GET /templates/{id}

Get full template with stages and tasks (including relative timing).

**Response 200:**

```json
{
  "id": 1,
  "title": "Become Backend Developer",
  "stages": [
    {
      "id": 1,
      "title": "Java Core",
      "orderNumber": 1,
      "tasks": [
        {
          "id": 1,
          "title": "Learn OOP",
          "daysOffset": 0,
          "durationDays": 7
        }
      ]
    }
  ]
}
```

---

### POST /goals

Create goal from template.

**Request:**

```json
{
  "templateId": 1,
  "title": "My Backend Journey",
  "startDate": "2026-05-01"
}
```

**Response 201:**

```json
{
  "id": 10,
  "title": "My Backend Journey",
  "status": "ACTIVE",
  "startDate": "2026-05-01",
  "endDate": "2026-07-01",
  "progress": 0
}
```

---

### GET /goals

Get current user goals.

**Response 200:**

```json
{
  "data": [
    {
      "id": 10,
      "title": "My Backend Journey",
      "status": "ACTIVE",
      "startDate": "2026-05-01",
      "endDate": "2026-07-01",
      "progress": 40
    }
  ]
}
```

**If empty:**

```json
{
  "data": []
}
```

---

### GET /goals/{id}

Get goal details.

**Access rules:**

* User can access only their own goals
* Otherwise → 403 Forbidden

**Response 200:**

```json
{
  "id": 10,
  "title": "My Backend Journey",
  "status": "ACTIVE",
  "startDate": "2026-05-01",
  "endDate": "2026-07-01",
  "stages": [
    {
      "id": 1,
      "title": "Java Core",
      "orderNumber": 1,
      "tasks": [
        {
          "id": 1,
          "title": "Learn OOP",
          "status": "IN_PROGRESS",
          "startDate": "2026-05-01",
          "endDate": "2026-05-07"
        }
      ]
    }
  ]
}
```

---

### PATCH /tasks/{id}

Update task status.

**Request:**

```json
{
  "status": "DONE"
}
```

**Allowed values:**

```
TODO, IN_PROGRESS, DONE
```

**Response 200:**

```json
{
  "id": 1,
  "status": "DONE"
}
```

---

### GET /analytics

Get user analytics.

**Response 200:**

```json
{
  "goalProgress": 60,
  "tasksDone": 12,
  "totalTasks": 20,
  "overdueTasks": 3,
  "completedGoals": 2
}
```

---

### POST /templates

Create template (admin).

**Request:**

```json
{
  "title": "Become Backend Developer",
  "description": "Full roadmap"
}
```

---

### POST /templates/{id}/stages

Add stage to template (admin).

**Request:**

```json
{
  "title": "Java Core",
  "orderNumber": 1
}
```

---

### POST /template-stages/{id}/tasks

Add task to template stage (admin).

**Request:**

```json
{
  "title": "Learn OOP",
  "orderNumber": 1,
  "daysOffset": 0,
  "durationDays": 7
}
```

---

### DELETE /templates/{id}

Delete template (admin).

**Response 204:**

```
No content
```

---

### GET /admin/users

Get users list (admin).

**Response 200:**

```json
{
  "data": [
    {
      "id": 1,
      "email": "user@example.com"
    }
  ]
}
```

---

### GET /admin/users/{id}/goals

Get user goals (admin).

**Response 200:**

```json
{
  "data": [
    {
      "id": 10,
      "title": "My Backend Journey",
      "status": "ACTIVE"
    }
  ]
}
```

---

## Error Responses

### 401 Unauthorized

```json
{
  "error": "Unauthorized"
}
```

### 403 Forbidden

```json
{
  "error": "Access denied"
}
```

### 404 Not Found

```json
{
  "error": "Resource not found"
}
```
