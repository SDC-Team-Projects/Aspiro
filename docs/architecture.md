# Architecture

## Overview

```text
┌─────────────┐     ┌────────────────────┐     ┌─────────────────────┐
│   Browser   │────▶│ Frontend           │────▶│ Backend             │
│             │     │(React + TypeScript)│     │ (Spring Boot)       │
└─────────────┘     └────────────────────┘     └──────────┬──────────┘
                                                          │
                                                   ┌──────▼──────┐
                                                   │ PostgreSQL  │
                                                   └─────────────┘
```

---

## Backend

The backend follows a layered architecture:

- **Controllers** – expose REST API endpoints.
- **Services** – contain the business logic.
- **Repositories** – provide database access using Spring Data JPA.
- **DTOs** – transfer data between the API and clients.
- **Mappers** – convert entities to DTOs and vice versa.
- **Domain Models** – represent application entities.
- **Security** – JWT authentication and authorization using Spring Security.
- **Exception Handling** – centralized error handling with custom exceptions and a global exception handler.

### Main packages

- `controller` – REST controllers
- `service` – business logic interfaces and implementations
- `repository` – database repositories
- `domain.model` – JPA entities
- `dto` – request and response objects
- `mapper` – entity/DTO mapping
- `security` – authentication, authorization, JWT filters
- `config` – application configuration
- `exception` – custom exceptions and error handling

---

## Frontend

The frontend is organized into feature-based modules:

- **Pages** – application routes.
- **Components** – reusable UI components.
- **API** – communication with the backend.
- **Types** – shared TypeScript interfaces.
- **Routing** – protected and admin routes.

### Main folders

- `pages` – application screens
- `components` – reusable React components
- `api` – API service modules
- `types` – TypeScript models and interfaces

---

## Authentication

Authentication is implemented using **JWT (JSON Web Tokens)**.

- Users authenticate through the login endpoint.
- The backend issues access tokens.
- Protected frontend routes require authentication.
- Spring Security validates JWT tokens for secured endpoints.

---

## Data Flow

```text
User
  │
  ▼
React Page
  │
  ▼
API Service
  │
  ▼
REST Controller
  │
  ▼
Service Layer
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```