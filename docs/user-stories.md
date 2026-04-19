## US-001: User Registration
**As** a new user
**I want** to register in the system
**So that** I can access the application features

### Acceptance Criteria
- [ ] Registration form includes email and password fields
- [ ] Email must be validated (correct format)
- [ ] Password must be at least 8 characters long
- [ ] User cannot register with an existing email
- [ ] Successful registration creates a new user in the system

## US-002: User Login
**As** a registered user
**I want** to log into the system
**So that** I can access my dashboard

### Acceptance Criteria
- [ ] Login form includes email and password
- [ ] System validates user credentials
- [ ] JWT token is generated upon successful login
- [ ] Invalid credentials show an error message

## US-003: View Goal Templates
**As** a user
**I want** to view available goal templates
**So that** I can choose a goal to follow

### Acceptance Criteria
- [ ] System displays a list of available templates
- [ ] Each template shows title and description
- [ ] Only approved/public templates are visible
- [ ] User can select a template

## US-004: Create Goal from Template
**As** a user
**I want** to create a goal from a template
**So that** I can start working on it

### Acceptance Criteria
- [ ] User selects a template
- [ ] System creates a new goal linked to the user
- [ ] All stages from template are copied to the goal
- [ ] All tasks from template are copied to stages
- [ ] Goal appears in the user dashboard

## US-005: View Goal Details
**As** a user
**I want** to view my goal details
**So that** I can understand my progress and structure

### Acceptance Criteria
- [ ] Goal details include title and status
- [ ] All stages are displayed in the correct order
- [ ] Tasks are grouped by stages
- [ ] Progress indicator is shown

## US-006: Mark Task as Completed
**As** a user
**I want** to mark a task as done
**So that** I can track my progress

### Acceptance Criteria
- [ ] User can change task status to “done”
- [ ] Task status is saved in the database
- [ ] UI reflects updated task status
- [ ] Only task status can be modified (not structure)

## US-007: Automatic Progress Calculation
**As** a user
**I want** the system to calculate progress automatically
**So that** I don’t have to do it manually

### Acceptance Criteria
- [ ] Stage progress is calculated as completed tasks / total tasks
- [ ] Goal progress is calculated as average of stage progress
- [ ] Progress updates immediately after task completion
- [ ] Progress is displayed in UI

## US-008: Goal Completion
**As** a user
**I want** the system to mark my goal as completed
**So that** I know I finished it

### Acceptance Criteria
- [ ] System checks if all tasks are completed
- [ ] If all tasks are done, goal status becomes “completed”
- [ ] Completed goals are visually distinguished

## US-009: View Analytics
**As** a user
**I want** to see analytics for my goals
**So that** I can evaluate my progress

### Acceptance Criteria
- [ ] System displays goal progress (%)
- [ ] Number of completed tasks is shown
- [ ] Total number of tasks is shown
- [ ] Analytics updates dynamically

## US-010: Admin Login
**As** an admin
**I want** to log into the system
**So that** I can manage templates

### Acceptance Criteria
- [ ] Admin uses login form
- [ ] Role is validated after login
- [ ] Admin is redirected to admin dashboard

## US-011: Create Goal Template
**As** an admin
**I want** to create a goal template
**So that** users can use it

### Acceptance Criteria
- [ ] Admin can enter template title and description
- [ ] Template is saved in the database
- [ ] Template is not available until fully created

## US-012: Add Stages to Template
**As** an admin
**I want** to add stages to a template
**So that** I can define goal structure

### Acceptance Criteria
- [ ] Admin can create multiple stages
- [ ] Each stage has a title
- [ ] Stages have order (order_index)
- [ ] Stages are linked to template

## US-013: Add Tasks to Template Stage
**As** an admin
**I want** to add tasks to each stage
**So that** I can define detailed steps

### Acceptance Criteria
- [ ] Admin can add tasks to a specific stage
- [ ] Each task has a title
- [ ] Tasks are linked to the correct stage

## US-014: Publish Template
**As** an admin
**I want** to make a template available to users
**So that** users can select it

### Acceptance Criteria
- [ ] Template can be marked as public
- [ ] Only public templates are visible to users
- [ ] Template appears in template list

## US-015: View Users and Goals (Admin)
**As** an admin
**I want** to view users and their goals
**So that** I can monitor system usage

### Acceptance Criteria
- [ ] Admin can view list of users
- [ ] Admin can view goals per user
- [ ] Data is read-only

## US-016: User Logout
**As** a user
**I want** to log out of the system
**So that** I can securely end my session

### Acceptance Criteria
- [ ] User can click logout button
- [ ] JWT token is removed on client side
- [ ] User is redirected to login page

## US-017: Access Control
**As** a system
**I want** to restrict access based on roles
**So that** users only access allowed features

### Acceptance Criteria
- [ ] User cannot access admin endpoints
- [ ] Admin-only endpoints require admin role
- [ ] Unauthorized access returns error (403)

## US-018: View Dashboard
**As** a user
**I want** to see my goals on dashboard
**So that** I can quickly access them

### Acceptance Criteria
- [ ] List of user goals is displayed
- [ ] Each goal shows progress
- [ ] User can open goal details

## US-019: Empty Dashboard State
**As** a new user
**I want** to see a message when I have no goals
**So that** I understand what to do next

### Acceptance Criteria
- [ ] Message like “No goals yet” is displayed
- [ ] Button to create goal is shown

## US-020: Error Handling
**As** a user
**I want** to see clear error messages
**So that** I understand what went wrong

### Acceptance Criteria
- [ ] API errors are handled gracefully
- [ ] User sees readable error messages
- [ ] System does not crash

## US-021: Data Isolation
**As** a user
**I want** to access only my own data
**So that** my data remains private

### Acceptance Criteria
- [ ] User cannot access other users’ goals
- [ ] API filters data by user_id
- [ ] Unauthorized access is denied