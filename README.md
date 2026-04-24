# Aspiro

## Description
Aspiro is a goal-oriented task management system where users choose a predefined template, follow structured stages and tasks, track progress, and complete goals.

## MVP Scope
- Templates are predefined (created by admin)
- Users cannot create templates
- No AI generation yet

## Core Concept

The system is based on two layers:

- **Template** — predefined structure (stages and tasks)
- **Goal** — user-specific instance created from a template

When a user creates a goal:
- Template stages are copied into goal stages
- Template tasks are copied into goal tasks

After that, the user works only with their own goal data.

## Project Structure

- `domain/model` — core domain entities
- `docs/` — documentation (database, diagrams)

## Tech Stack
- Java 21
- Spring Boot
- Gradle

## Run

```bash
gradlew.bat build
gradlew.bat bootRun
```


