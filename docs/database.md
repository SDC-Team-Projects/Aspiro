# Database Overview

This database is designed for a goal-oriented task management system.

The system is divided into two layers:

- **Template layer** — predefined structures created by admin
- **Goal layer** — user-specific instances created from templates

Templates do not store real dates.  
Template tasks store relative timing using `days_offset` and `duration_days`.

When a user creates a goal, the system calculates real dates for goal tasks based on the goal start date.

---

## Enums

### user_role
- USER
- ADMIN

### goal_status
- ACTIVE
- COMPLETED
- OVERDUE

### task_status
- TODO
- IN_PROGRESS
- DONE
- OVERDUE

---

## user

|  Column  |   Type    |     Description    |
|----------|-----------|--------------------|
| id       | BIGINT    | PK                 |
| name     | VARCHAR   | Not null           |
| email    | VARCHAR   | Unique, not null   |
| password | VARCHAR   | Not null           |
| role     | ENUM      | USER, ADMIN        |

---

## templates

|   Column    |   Type  |   Description   |
|-------------|---------|-----------------|
| id          | BIGINT  | PK              |
| title       | VARCHAR | Not null        |
| description | TEXT    | Nullable        |

---

## template_stage

|    Column    |   Type  |        Description          |
|--------------|---------|-----------------------------|
| id           | BIGINT  | PK                          |
| title        | VARCHAR | Not null                    |
| order_number | INT     | Stage order inside template |
| template_id  | BIGINT  | FK → templates.id           |

---

## template_task

|     Column        |     Type     |            Description              |
|-------------------|--------------|-------------------------------------|
| id                | BIGINT       | PK                                  |
| title             | VARCHAR      | Not null                            |
| description       | TEXT         | Nullable                            |
| order_number      | INT          | Task order inside template stage    |
| days_offset       | INT          | Number of days from goal start date |
| duration_days     | INT          | Task duration in days               |
| template_stage_id | BIGINT       | FK → template_stages.id             |

---

## goal

|     Column    |     Type    |        Description         |
|---------------|-------------|----------------------------|
| id            | BIGINT      | PK                         |
| title         | VARCHAR     | Not null                   |
| start_date    | DATE        | Goal start date            |
| end_date      | DATE        | Calculated goal end date   |
| status        | ENUM        | ACTIVE, COMPLETED, OVERDUE |
| user_id       | BIGINT      | FK → users.id              |
| template_id   | BIGINT      | FK → templates.id          |

---

## goal_stage

|   Column          |    Type   |      Description        |
|-------------------|-----------|-------------------------|
| id                | BIGINT    | PK                      |
| title             | VARCHAR   | Not null                |
| order_number      | INT       | Stage order inside goal |
| goal_id           | BIGINT    | FK → goals.id           |
| template_stage_id | BIGINT    | FK → template_stages.id |

---

## goal_task

| Column           | Type     |                   Description                   |
|------------------|----------|-------------------------------------------------|
| id               | BIGINT   | PK                                              |
| title            | VARCHAR  | Not null                                        |
| description      | TEXT     | Nullable                                        |
| status           | ENUM     | TODO, IN_PROGRESS, DONE, OVERDUE                |
| start_date       | DATE     | Calculated from goal start date and task offset |
| end_date         | DATE     | Calculated from start date and task duration    |
| order_number     | INT      | Task order inside goal stage                    |
| goal_stage_id    | BIGINT   | FK → goal_stages.id                             |
| template_task_id | BIGINT   | FK → template_tasks.id                          |

---

## Relationships

- `users` 1 : N `goals` — one user can have many goals
- `templates` 1 : N `goals` — one template can be used for many goals
- `templates` 1 : N `template_stages` — one template contains many template stages
- `template_stages` 1 : N `template_tasks` — one template stage contains many template tasks
- `goals` 1 : N `goal_stages` — one goal contains many goal stages
- `goal_stages` 1 : N `goal_tasks` — one goal stage contains many goal tasks
- `template_stages` 1 : N `goal_stages` — one template stage can be copied into many goal stages
- `template_tasks` 1 : N `goal_tasks` — one template task can be copied into many goal tasks

---

## Data Flow

1. Admin creates templates with stages and tasks
2. Template tasks store relative timing:
   - `days_offset`
   - `duration_days`
3. User selects a template and sets a goal start date
4. System creates a goal
5. Template stages are copied into goal stages
6. Template tasks are copied into goal tasks
7. Goal task dates are calculated:
   - `goal_task.start_date = goal.start_date + template_task.days_offset`
   - `goal_task.end_date = goal_task.start_date + template_task.duration_days`
8. Goal end date can be calculated from the latest goal task end date
9. User works only with goal data: task status, progress, and dates