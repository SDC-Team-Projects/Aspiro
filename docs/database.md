## Database Overview

```md
This database is designed for a Task and Project Management system.

The database is divided into two layers: templates and goals.  
A user selects a template, which contains a title, description, and a set of stages with tasks.  

When a template is selected, a personal instance of it is created as a goal.  
All stages and tasks are copied into this goal.  

This allows users to work with their own independent copy of tasks without modifying the original template.

---

## Tables and Description

```text
Enum user_role {
  USER
  ADMIN
}

Enum goal_status {
  ACTIVE
  COMPLETED
  OVERDUE
}

Enum task_status {
  TODO
  IN_PROGRESS
  DONE
  OVERDUE
}
```
### Users

Stores application users and their roles.
Table users {
  id long [pk, increment]
  name varchar [not null]
  email varchar [unique, not null]
  password varchar [not null]
  role user_role [not null]
}

### Templates

Predefined goal templates with stages and tasks.
Table templates {
  id long [pk, increment]
  title varchar [not null]
  description text
}

### Template Stages

Defines stages within a template.
Table template_stages {
  id long [pk, increment]
  title varchar [not null]
  order_number int [not null]
  template_id long [not null]
}

### Template Tasks

Defines tasks within each template stage.
Table template_tasks {
  id long [pk, increment]
  title varchar [not null]
  description text
  order_number int
  template_stage_id long [not null]
}

### Goals

Represents user goals created from templates.
Table goals {
  id long [pk, increment]
  title varchar [not null]
  deadline timestamp
  status goal_status [not null]
  user_id long [not null]
  template_id long [not null]
}

### Goal Stages

Represents user-specific stages created from template stages.
Table goal_stages {
  id long [pk, increment]
  title varchar [not null]
  order_number int [not null]
  goal_id long [not null]
  template_stage_id long
}

### Goal Tasks

Represents user-specific tasks with progress tracking.
Table goal_tasks {
  id long [pk, increment]
  title varchar [not null]
  description text
  status task_status [not null]
  deadline timestamp
  order_number int
  goal_stage_id long [not null]
  template_task_id long
}

## Technical Relationships
Ref: template_stages.template_id > templates.id
Ref: template_tasks.template_stage_id > template_stages.id

Ref: goals.user_id > users.id
Ref: goals.template_id > templates.id

Ref: goal_stages.goal_id > goals.id
Ref: goal_stages.template_stage_id > template_stages.id

Ref: goal_tasks.goal_stage_id > goal_stages.id
Ref: goal_tasks.template_task_id > template_tasks.id


## Relationships 
- A User can have multiple Goals
- A Goal is created based on a Template
- A Template contains multiple TemplateStages
- A TemplateStage contains multiple TemplateTasks
- A Goal contains multiple GoalStages
- A GoalStage contains multiple GoalTasks

When a user creates a goal from a template, stages and tasks are generated based on the template structure.
Each goal maintains its own independent progress and task states.
